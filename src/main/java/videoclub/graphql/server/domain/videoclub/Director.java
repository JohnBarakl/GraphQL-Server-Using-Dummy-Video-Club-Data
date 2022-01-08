package videoclub.graphql.server.domain.videoclub;

/**
 * Defines a Director object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */

public class Director {
    Integer id; // The director's unique identifier.
    String name; // The director's name.

    /**
     * Builds a Director object and sets its fields according to the arguments.
     * @param id The director's unique identifier.
     * @param name The director's name.
     */
    public Director(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
