package videoclub.datastore.DataPoints;


import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.Customer;
import videoclub.graphql.server.domain.videoclub.MovieCopy;
import videoclub.graphql.server.domain.videoclub.RentTransaction;
import videoclub.graphql.server.domain.videoclub.input.NewRentingInput;
import videoclub.graphql.server.domain.videoclub.input.ReturnInput;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * The class that manages the application's data concerning the Rent Transactions. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class RentTransactionData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public RentTransactionData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Executes the given query and returns the list of RentTransaction object created.
     * @param query The sql query to be executed.
     * @return The list of RentTransaction objects retrieved.
     */
    private RentTransaction[] executeRentTransactionRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<RentTransaction> rentTransactions = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query.toString());

            // Running through the results and constructing Customer objects with the returned data.
            while (qResults.next()) {
                rentTransactions.add(new RentTransaction(qResults.getInt(1), qResults.getFloat(2),
                        OffsetDateTime.parse(qResults.getString(3)),
                        qResults.getString(4) != null ? OffsetDateTime.parse(qResults.getString(4)) : null));
            }
        }

        return rentTransactions.toArray(new RentTransaction[0]);
    }

    /**
     * Retrieves a number of rent transactions from the database.
     * @param rentTransaction The rent transaction template that will be used for selection of the actors loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     *              <p>Note: if the dateTo field has the value OffsetDateTime.MAX, then the results will be filtered so that
     *                       the column dateTo is null.</p>
     * @return A list of RentTransaction entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public RentTransaction[] retrieveRentTransactions(RentTransaction rentTransaction) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, price, dateFrom, dateTo ")
                .append("from RentTransaction ");

        // Controls whether the final query will retrieve all rent transactions or will filter the results first.
        boolean getAll = true;

        // Check if it has been given an id filter.
        if (rentTransaction.getId() != null){
            getAll = false;
            sqlQuery.append("where id = ").append(rentTransaction.getId());
        }

        // Check if it has been given a price filter.
        if (rentTransaction.getPrice() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" price = \"").append( rentTransaction.getPrice() ).append("\"");
        }

        // Check if it has been given a dateFrom filter.
        if (rentTransaction.getDateFrom() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" dateFrom = \"").append( rentTransaction.getDateFrom() ).append("\"");
        }

        // Check if it has been given a dateTo filter.
        if (rentTransaction.getDateTo() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            // OffsetDateTime.MAX signal value of null
            if (rentTransaction.getDateTo().equals(OffsetDateTime.MAX)){
                sqlQuery.append(" dateTo is null");
            } else {
                sqlQuery.append(" dateTo = \"").append( rentTransaction.getDateTo() ).append("\"");
            }

        }

        return executeRentTransactionRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Returns a list of rent transactions that involve a customer.
     * @param customer The customer that is referenced.
     * @return A list of RentTransaction entities.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the ProductionCompany object fields.
     */
    public RentTransaction[] retrieveRentTransactionsFromCustomer(Customer customer) throws SQLException, IllegalArgumentException  {

        // An actor object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (customer.getId() == null){
            throw new IllegalArgumentException("The CustomerId must not be null.");
        }

        String sqlQuery = String.format("select id, price, dateFrom, dateTo " +
                "from RentTransaction " +
                "where Customer_id = %d", customer.getId());

        return executeRentTransactionRetrievalQuery(sqlQuery);
    }

    /**
     * Returns a list of rent transactions that involve a movie copy.
     * @param movieCopy The movie copy that is referenced.
     * @return A list of RentTransaction entities.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the ProductionCompany object fields.
     */
    public RentTransaction[] retrieveRentTransactionsFromMovieCopy(MovieCopy movieCopy) throws SQLException, IllegalArgumentException  {

        // An actor object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieCopy.getId() == null){
            throw new IllegalArgumentException("The MovieCopyId must not be null.");
        }

        String sqlQuery = String.format("select id, price, dateFrom, dateTo " +
                "from RentTransaction " +
                "where MovieCopy_id = %d ", movieCopy.getId());

        return executeRentTransactionRetrievalQuery(sqlQuery);
    }

    /**
     * Returns a list of rent transactions from the database and filters them with the given arguments before returning
     * the list with them.
     * @param priceFrom price lower limit. null value means no lower limit.
     * @param priceTo price upper limit. null value means no upper limit.
     * @param dateFrom date lower limit. null value means no lower limit.
     * @param dateTo date upper limit. null value means no upper limit.
     * @return A list with the filtered data.
     * @throws SQLException If a database access error occurs.
     */
    public RentTransaction[] retrieveRentTransactionsFiltered(Float priceFrom, Float priceTo, OffsetDateTime dateFrom, OffsetDateTime dateTo) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, price, dateFrom, dateTo ")
                .append("from RentTransaction ");

        // Controls whether the final query will retrieve all rent transactions or will filter the results first.
        boolean getAll = true;

        // Check if it has been given a priceFrom filter.
        if (priceFrom != null){
            // If it has another filter to chain into where condition.
            getAll = false;
            sqlQuery.append("where ");

            sqlQuery.append(" price >= ").append( priceFrom ).append(" ");
        }

        // Check if it has been given a priceTo filter.
        if (priceTo != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" price <= ").append( priceTo ).append(" ");
        }

        // Check if it has been given a dateFrom filter.
        if (dateFrom != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" dateFrom >= \"").append( dateFrom ).append("\"");
        }

        // Check if it has been given a dateTo filter.
        if (dateTo != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" dateTo <= \"").append( dateTo ).append("\"");

        }

        return executeRentTransactionRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Creates a new rentTransaction in the database and returns the corresponding object.
     * @param newRentingInput The input object.
     * @return the new rentTransaction inserted in the database.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public RentTransaction insertRentTransaction(NewRentingInput newRentingInput) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        // Check if customerID was provided.
        if (newRentingInput.getCustomerID() == null){
            throw new IllegalArgumentException("Customer id required.");
        } else {
            columns.append("Customer_id, ");
            values.append("'").append(newRentingInput.getCustomerID()).append("', ");
        }

        // Check if movieCopyID was provided.
        if (newRentingInput.getCustomerID() == null){
            throw new IllegalArgumentException("Movie copy id required.");
        } else {
            columns.append("MovieCopy_id, ");
            values.append("'").append(newRentingInput.getMovieCopyID()).append("', ");
        }

        // Check if price was provided.
        if (newRentingInput.getPrice() != null){
            columns.append("price, ");
            values.append("'").append(newRentingInput.getPrice()).append("', ");
        }

        // Check if dateFrom was provided.
        if (newRentingInput.getDateFrom() == null){
            throw new IllegalArgumentException("Date from required.");
        } else {
            columns.append("dateFrom, ");
            values.append("'").append(newRentingInput.getDateFrom()).append("'', ");
        }


        columns.replace(columns.length() - 2, columns.length(), "");
        values.replace(values.length() - 2, values.length(), "");

        String query = String.format("insert into RentTransaction ( %s ) %n" +
                "values ( %s )", columns.toString(), values.toString());

        Integer newRentId;

        // Making sure there are no synchronization errors.
        synchronized (queryEndpoint) {
            queryEndpoint.execute(query);

            // Query the database for all the valid ids. The one with the greatest value is the one just inserted.
            String newIdQuery = "select id " +
                                "from RentTransaction " +
                                "order by id desc limit 1";

            ResultSet qResults = queryEndpoint.executeQuery(newIdQuery);

            newRentId = qResults.getInt(0);
        }
        // Searching and returning the new customer object by id.
        // Only one is expected to be returned.
        return retrieveRentTransactions(new RentTransaction(newRentId, null, null, null))[0];
    }

    /**
     * Updates a {@link RentTransaction} object with a return date (only if it has none).
     * @param returnInput the id and return date of transaction.
     * @return the updated object.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing or the
     *                                  transaction already has a return date.
     */
    public RentTransaction updateRentTransaction(ReturnInput returnInput) throws SQLException {
        // Check if id was provided.
        if (returnInput.getRentID() == null){
            throw new IllegalArgumentException("Rent Transaction id required.");
        }

        // Retrieve the specified transaction.
        RentTransaction specifiedTransaction = retrieveRentTransactions(
                new RentTransaction(returnInput.getRentID(), null, null, null)
        )[0];

        // If the transaction already has an end date then it is already terminated: Invalid input.
        if (specifiedTransaction.getDateTo() != null){
            throw new IllegalArgumentException("Rent Transaction already completed.");
        }

        // Check if the given date is null
        if (returnInput.getDate() == null){
            throw new IllegalArgumentException("Return date required.");
        }

        // Making sure there are no synchronization errors.
        synchronized (queryEndpoint) {
            String query ="update RentTransaction " +
                          "set dateTo = '" + returnInput.getDate().toString() + "'" +
                          " where id = " + returnInput.getRentID();

            queryEndpoint.execute(query);
        }

        // Return the mutated object.
        return retrieveRentTransactions(specifiedTransaction)[0];
    }
}
