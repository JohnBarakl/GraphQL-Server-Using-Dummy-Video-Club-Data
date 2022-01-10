package videoclub.graphql.server.resolvers.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.Customer;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Resolver for the query getCustomerByID.
 * It returns the Customer object(s) (from the database) that match the arguments.
 * If no arguments are provided it returns all customers.
 * @author Ioannis Baraklilis
 */
@Component
public class GetCustomerResolver implements GraphQLQueryResolver {
    public Customer[] getCustomer(Integer id, String fullName) throws SQLException {
        Customer template = new Customer(id, fullName, null, null, null, null);
        return Application.dataSource.retrieveCustomers(template);
    }
}
