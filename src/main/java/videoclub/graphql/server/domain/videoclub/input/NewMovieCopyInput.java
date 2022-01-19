package videoclub.graphql.server.domain.videoclub.input;

import videoclub.graphql.server.domain.videoclub.MovieCopy;

/**
 * The object equivalent to the input type of the same name used to accept input for the purposes
 * of creating a new movie copy.
 */
public class NewMovieCopyInput {

    /** The movie title that this copy contains. */
    Integer movieTitleId;

    /** The copy's physical medium type. */
    MovieCopy.Medium medium;

    /** The copy's format. */
    MovieCopy.MovieFormat copyType;

    /** The price this movie is being rented for. */
    Float rentPrice;

    public Integer getMovieTitleId() {
        return movieTitleId;
    }

    public MovieCopy.Medium getMedium() {
        return medium;
    }

    public MovieCopy.MovieFormat getCopyType() {
        return copyType;
    }

    public Float getRentPrice() {
        return rentPrice;
    }
}
