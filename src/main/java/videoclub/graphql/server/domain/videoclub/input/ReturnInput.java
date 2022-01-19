package videoclub.graphql.server.domain.videoclub.input;

import java.time.OffsetDateTime;

/**
 * The object equivalent to the input type of the same name used to accept input for the purposes
 * of modifying a rent transaction to note its termination.
 */
public class ReturnInput {
    /** The rent statement's unique identifier */
    Integer rentID;

    /** The return date of the movie copy. The termination of rent. */
    OffsetDateTime date;

    public Integer getRentID() {
        return rentID;
    }

    public OffsetDateTime getDate() {
        return date;
    }

}
