package videoclub.graphql.server.domain.videoclub;

import java.time.OffsetDateTime;

/**
 * Defines a RentTransaction object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */
public class RentTransaction {
    Integer id; // The statement's unique identifier.
    Customer customer; // The customer that made this transaction.
    MovieCopy movie; // The movie copy that the customer rented.
    Float price; // The price (per day) of the rent.
    OffsetDateTime dateFrom; // The date when the movie copy was rented
    OffsetDateTime dateTo; // The date when the movie copy was returned.

    /**
     * Builds a RentTransaction object and sets its fields according to the arguments.
     * @param id The statement's unique identifier.
     * @param customer The customer that made this transaction.
     * @param movie The movie copy that the customer rented.
     * @param price The price (per day) of the rent.
     * @param dateFrom The date when the movie copy was rented
     * @param dateTo The date when the movie copy was returned.
     *               <p>If null then it still is being rented.</p>
     */
    public RentTransaction(Integer id, Customer customer, MovieCopy movie, Float price, OffsetDateTime dateFrom, OffsetDateTime dateTo) {
        this.id = id;
        this.customer = customer;
        this.movie = movie;
        this.price = price;
        this.dateFrom = dateFrom;
        this.dateTo = dateTo;
    }
}
