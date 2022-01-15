package videoclub.graphql.server.resolvers.fields;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.Customer;
import videoclub.graphql.server.domain.videoclub.MovieCopy;
import videoclub.graphql.server.domain.videoclub.RentTransaction;

import java.sql.SQLException;

/**
 * This class contains methods to resolve field queries of the type RentTransaction that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class RentTransactionFieldsResolver implements GraphQLResolver<RentTransaction> {

    /**
     * Acts as resolver for the customer field of RentTransaction type.
     * @param rentTransaction The rentTransaction that refers to the requested customer.
     * @return The customer that participates in the transaction.
     * @throws SQLException If there is a communication error with the data source.
     */
    public Customer customer(RentTransaction rentTransaction) throws SQLException {
        return Application.dataSource.aboutCustomers().retrieveCustomerFromTransaction(rentTransaction);
    }

    /**
     * Acts as resolver for the movie field of RentTransaction type.
     * @param rentTransaction The rentTransaction that refers to the requested customer.
     * @return The movie that participates in the transaction.
     * @throws SQLException If there is a communication error with the data source.
     */
    public MovieCopy movie(RentTransaction rentTransaction) throws SQLException {
        return Application.dataSource.aboutMovieCopies().retrieveMovieCopyFromTransaction(rentTransaction);
    }
}
