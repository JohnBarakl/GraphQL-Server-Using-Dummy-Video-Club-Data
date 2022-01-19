package videoclub.graphql.server.resolvers.mutation;

import graphql.kickstart.tools.GraphQLMutationResolver;
import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.*;
import videoclub.graphql.server.domain.videoclub.input.*;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Resolver for the mutations supported by the server.
 * @author Ioannis Baraklilis
 */
@Component
public class MutationResolver implements GraphQLMutationResolver {
    /**
     * Creates and inserts a new customer based on the data given.
     * @param input The input object on which the new customer will be created.
     * @return the new customer as an object.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public Customer createCustomer(CreateCustomerInput input) throws SQLException {
        return Application.dataSource.aboutCustomers().insertCustomer(input);
    }

    /**
     * Creates and inserts a new RentTransaction based on the data given.
     * @param input The input object on which the new RentTransaction will be created.
     * @return the new rent transaction created as an object.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public RentTransaction registerNewRenting(NewRentingInput input) throws SQLException {
        return Application.dataSource.aboutRentTransactions().insertRentTransaction(input);
    }

    /**
     * Register a return of rented media.
     * @param returnInput the input from user.
     * @return the updated object.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public RentTransaction registerReturn(ReturnInput returnInput) throws SQLException {
        return Application.dataSource.aboutRentTransactions().updateRentTransaction(returnInput);
    }

    /**
     * Creates and inserts a new movie copy based on the data given.
     * @param input The input object on which the new movie copy will be created.
     * @return the new movie copy created, as an object.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public MovieCopy registerNewMovieCopy(NewMovieCopyInput input) throws SQLException {
        return Application.dataSource.aboutMovieCopies().insertNewMovieCopy(input);
    }

    /**
     * Deletes an existing movie copy based on the data given.
     * @param input The input object on which the movie copy will be deleted.
     * @return the success state of the deleting process.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public Boolean deleteMovieCopy(DeleteMovieCopyInput input) throws SQLException {
        return Application.dataSource.aboutMovieCopies().deleteMovieCopy(input);
    }
}
