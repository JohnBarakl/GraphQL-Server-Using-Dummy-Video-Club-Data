package videoclub.graphql.server.domain.videoclub.input;

import java.time.OffsetDateTime;

/**
 * The object equivalent to the input type of the same name used to accept input for the purposes
 * of creating a new rent transaction.
 */
public class NewRentingInput {
    /** The id of the customer that rents a movie copy. */
    Integer customerID;

    /** The id of the movie copy being rented. */
    Integer movieCopyID;

    /** The price (per day) of the rent. If null, then the movieCopy's price will be inserted instead. */
    Float price;

    /** The date and time when the movie copy was rented */
    OffsetDateTime dateFrom;

    public Integer getCustomerID() {
        return customerID;
    }

    public Integer getMovieCopyID() {
        return movieCopyID;
    }

    public Float getPrice() {
        return price;
    }

    public OffsetDateTime getDateFrom() {
        return dateFrom;
    }
}
