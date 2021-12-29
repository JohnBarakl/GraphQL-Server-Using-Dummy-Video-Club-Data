package videoclub.graphql.server.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.server.domain.videoclub.Customer;
import videoclub.graphql.server.domain.videoclub.RentTransaction;

@Component
public class RentTransactionsResolver implements GraphQLResolver<Customer> {
    /**
     * Defines a rentTransaction status.
     */
    public enum TransactionStatus {
        /** Defines all transactions that have been completed.
         *  For example, a completed rent. A completed rent means that a customer rented a movie and then returned it. */
        Completed,

        /** Defines all transactions that have not been completed.
         *  For example, an incomplete rent. A incomplete rent means that a customer rented a movie and has not yet returned it.*/
        Active,

        /** Defines all transactions. Superset of Completed an Active.*/
        All
    }

    public RentTransaction[] rentTransactions(Customer customer, TransactionStatus status){
        // TODO: implement
        return new RentTransaction[0];
    }
}
