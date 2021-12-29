package videoclub.graphql.server.domain.videoclub;

/**
 * Defines a Movie Copy object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */
public class MovieCopy {
    /**
     * Describes the physical medium the copy of a movie can be.
     * @author Ioannis Baraklilis
     */
    public enum Medium{
        DVD,
        CD,
        BlueRay,
        HD_DVD,
        Cassette
    }

    /**
     * Describes the format of a movie
     * @author Ioannis Baraklilis
     */
    public enum MovieFormat {
        SD,
        HD,
        T3D // Defines a 3D movie type.
    }

    Integer id; // The movie copy's unique identifier.
    MovieCopy.Medium medium; // The copy's physical medium type.
    MovieCopy.MovieFormat copyType; // The copy's format.
    Float rentPrice; // The price this movie is being rented for.

    /**
     * Builds a MovieCopy object and sets its fields according to the arguments.
     * @param id The movie copy's unique identifier.
     * @param medium The copy's physical medium type.
     * @param copyType The copy's format.
     * @param rentPrice The price this movie is being rented for.
     */
    public MovieCopy(Integer id, Medium medium, MovieFormat copyType, Float rentPrice) {
        this.id = id;
        this.medium = medium;
        this.copyType = copyType;
        this.rentPrice = rentPrice;
    }
}
