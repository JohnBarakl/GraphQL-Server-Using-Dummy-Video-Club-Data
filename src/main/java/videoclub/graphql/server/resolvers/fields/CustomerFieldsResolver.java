package videoclub.graphql.server.resolvers.fields;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.Customer;
import videoclub.graphql.server.domain.videoclub.RentTransaction;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * This class contains methods to resolve field queries of the type Customer that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class CustomerFieldsResolver implements GraphQLResolver<Customer> {
    /**
     * Defines a rentTransaction status.
     */
    public enum TransactionStatus {
        /** Defines all transactions that have been completed.
         *  For example, a completed rent. A completed rent means that a customer rented a movie and then returned it. */
        Completed,

        /** Defines all transactions that have not been completed.
         *  For example, an incomplete rent. An incomplete rent means that a customer rented a movie and has not yet returned it.*/
        Active,

        /** Defines all transactions. Superset of Completed an Active.*/
        All
    }

    /**
     * Acts as resolver for the rentTransactions field of customer type.
     * @param customer The customer whose rentTransactions are requested.
     * @param status The status of rentTransactions to be returned.
     * @return a list of the requested rentTransactions.
     * @throws SQLException if there is a communication error with the data source.
     */
    public RentTransaction[] rentTransactions(Customer customer, TransactionStatus status) throws SQLException {
        // No need to filter out results, return the whole list.
        if (status == TransactionStatus.All){
            return Application.dataSource.aboutRentTransactions().retrieveRentTransactionsFromCustomer(customer);
        } else if (status == TransactionStatus.Active){ // Filter out completed transactions.
            ArrayList<RentTransaction> queryResults = new ArrayList<>(
                    List.of(Application.dataSource.aboutRentTransactions().retrieveRentTransactionsFromCustomer(customer))
            );

            Iterator<RentTransaction> transactionIterator = queryResults.iterator();
            while (transactionIterator.hasNext()){
                RentTransaction nextElement = transactionIterator.next();

                if (nextElement.getDateTo() == null){
                    transactionIterator.remove();
                }
            }

            return queryResults.toArray(new RentTransaction[0]);
        } else { // Filter out incomplete transactions.
            ArrayList<RentTransaction> queryResults = new ArrayList<>(
                    List.of(Application.dataSource.aboutRentTransactions().retrieveRentTransactionsFromCustomer(customer))
            );

            Iterator<RentTransaction> transactionIterator = queryResults.iterator();
            while (transactionIterator.hasNext()){
                RentTransaction nextElement = transactionIterator.next();

                if (nextElement.getDateTo() != null){
                    transactionIterator.remove();
                }
            }

            return queryResults.toArray(new RentTransaction[0]);
        }
    }

}
