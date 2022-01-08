package videoclub.graphql.server.domain.videoclub;

/**
 * Defines an Actor object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */
public class Actor {
    Integer id; // The actor's unique identifier.
    String name; // The actor's name.

    /**
     * Builds an Actor object and sets its fields according to the arguments.
     * @param id The actor's unique identifier.
     * @param name The actor's name.
     */
    public Actor(Integer id, String name) {
        this.id = id;
        this.name = name;
    }

    public Integer getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    @Override
    public String toString() {
        return "Actor{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
