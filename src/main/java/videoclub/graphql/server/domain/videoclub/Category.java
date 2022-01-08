package videoclub.graphql.server.domain.videoclub;

/**
 * Defines a Category object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */
public class Category {
    Integer id; // The category's unique identifier.
    String name; // The category's name.

    /**
     * Builds a Category object and sets its fields according to the arguments.
     * @param id The category's unique identifier.
     * @param name The category's name.
     */
    public Category(Integer id, String name) {
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
