package videoclub.datastore.DataPoints;

import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.Customer;
import videoclub.graphql.server.domain.videoclub.RentTransaction;
import videoclub.graphql.server.domain.videoclub.input.CreateCustomerInput;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class that manages the application's data concerning the Customers. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class CustomerData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public CustomerData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Executes the given query and returns the list of customer object created.
     * @param query The sql query to be executed.
     * @return The list of Customers objects retrieved.
     */
    private Customer[] executeCustomerRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<Customer> customers = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query.toString());

            // Running through the results and constructing Customer objects with the returned data.
            while (qResults.next()) {
                customers.add(new Customer(qResults.getInt(1), qResults.getString(2),
                        qResults.getString(3)==null?null:LocalDate.parse(qResults.getString(3)), qResults.getString(4),
                        qResults.getString(5), qResults.getString(6)));
            }
        }

        return customers.toArray(new Customer[0]);
    }

    /**
     * Retrieves a number of customers from the database.
     * @param customer The customer template that will be used for selection of the customers loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of Customer entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public Customer[] retrieveCustomers(Customer customer) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, fullName, dateOfBirth, address, phoneNumber, email ")
                .append("from Customer ");

        // Controls whether the final query will retrieve all customers or will filter the results first.
        boolean getAll = true;

        // Check if it has been given an id filter.
        if (customer.getId() != null){
            getAll = false;
            sqlQuery.append("where id = ").append(customer.getId());
        }

        // Check if it has been given an fullName filter.
        if (customer.getFullName() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" fullName = \"").append(customer.getFullName()).append("\"");
        }

        // Check if it has been given a dateOfBirth filter.
        if (customer.getDateOfBirth() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" dateOfBirth = \"").append(customer.getDateOfBirth()).append("\"");
        }

        // Check if it has been given a phoneNumber filter.
        if (customer.getPhoneNumber() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" phoneNumber = \"").append(customer.getPhoneNumber()).append("\"");
        }

        // Check if it has been given an email filter.
        if (customer.getEmail() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" email = \"").append(customer.getEmail()).append("\"");
        }

        return executeCustomerRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Retrieves the customer involved in the transaction of the argument.
     * @param rentTransaction The rent transaction that involves a customer.
     * @return A Customer entity.
     * @throws SQLException If a database access error occurs.
     */
    public Customer retrieveCustomerFromTransaction(RentTransaction rentTransaction) throws SQLException {
        // A rent transaction object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (rentTransaction.getId() == null){
            throw new IllegalArgumentException("The RentTransactionId must not be null.");
        }

        String sqlQuery = "select Customer.id, Customer.fullName, Customer.dateOfBirth, Customer.address, Customer.phoneNumber, Customer.email " +
                "from Customer " +
                "where Customer.id = (select Customer_id from RentTransaction where id = " + rentTransaction.getId() + " )";

        return executeCustomerRetrievalQuery(sqlQuery)[0];
    }

    /**
     * Creates a new customer in the database and returns the corresponding object.
     * @param customerInput The input object.
     * @return the new customer inserted in the database.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public Customer insertCustomer(CreateCustomerInput customerInput) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        // Check if full name was provided.
        if (customerInput.getFullName() == null){
            throw new IllegalArgumentException("Customer full name required.");
        } else {
            columns.append("fullName, ");
            values.append("'").append(customerInput.getFullName()).append("', ");
        }

        // Check if date of birth was provided.
        if (customerInput.getDateOfBirth() != null){
            columns.append("dateOfBirth, ");
            values.append("'").append(customerInput.getDateOfBirth()).append("', ");
        }

        // Check if address was provided.
        if (customerInput.getAddress() == null){
            throw new IllegalArgumentException("Address required.");
        } else {
            columns.append("address, ");
            values.append("'").append(customerInput.getAddress()).append("', ");
        }

        // Check if phone number was provided.
        if (customerInput.getPhoneNumber() == null){
            throw new IllegalArgumentException("Phone number required.");
        } else {
            columns.append("phoneNumber, ");
            values.append("'").append(customerInput.getPhoneNumber()).append("', ");
        }

        // Check if email was provided.
        if (customerInput.getEmail() != null){
            columns.append("email, ");
            values.append("'").append(customerInput.getEmail()).append("', ");
        }

        columns.replace(columns.length() - 2, columns.length(), "");
        values.replace(values.length() - 2, values.length(), "");


        String query = String.format("insert into Customer ( %s ) %n" +
                                     "values ( %s )", columns.toString(), values.toString());

        Integer newCustomerId;

        // Making sure there are no synchronization errors.
        synchronized (queryEndpoint) {
            queryEndpoint.execute(query);

            // Query the database for all the valid ids. The one with the greatest value is the one just inserted.
            String newIdQuery = "select id " +
                                "from Customer " +
                                "order by id desc limit 1";

            ResultSet qResults = queryEndpoint.executeQuery(newIdQuery);

            newCustomerId = qResults.getInt(1);
        }
        // Searching and returning the new customer object by id.
        // Only one is expected to be returned.
        return retrieveCustomers(new Customer(newCustomerId, null, null, null, null, null))[0];
    }
}
