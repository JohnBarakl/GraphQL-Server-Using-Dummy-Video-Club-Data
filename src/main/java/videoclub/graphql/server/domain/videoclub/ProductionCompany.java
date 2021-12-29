package videoclub.graphql.server.domain.videoclub;

/**
 * Defines a ProductionCompany object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */
public class ProductionCompany {
    Integer id; // The production company's unique identifier.
    String name; // The production company's name.

    /**
     * Builds a ProductionCompany object and sets its fields according to the arguments.
     * @param id The production company's unique identifier.
     * @param name The production company's name.
     */
    public ProductionCompany(Integer id, String name, MovieTitle[] moviesProduced) {
        this.id = id;
        this.name = name;
    }
}
