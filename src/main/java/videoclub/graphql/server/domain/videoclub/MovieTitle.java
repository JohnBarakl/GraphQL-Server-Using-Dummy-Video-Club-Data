package videoclub.graphql.server.domain.videoclub;

import java.time.LocalDate;

/**
 * Defines a MovieTitle object equivalent of the one defined in the GraphQL schema.
 * @author Ioannis Baraklilis
 */
public class MovieTitle {
    Integer id; // The movie's unique identifier.
    String title; // The movie's title.
    String description; // The movie's description.
    LocalDate releaseDate; // The movie's release date.
    Float rating; // The movie's rating.

    /**
     * Builds a MovieTitle object and sets its fields according to the arguments.
     * @param id The movie's unique identifier.
     * @param title The movie's title.
     * @param description The movie's description.
     * @param releaseDate The movie's release date.
     * @param rating The movie's rating.
     */
    public MovieTitle(Integer id, String title, String description, LocalDate releaseDate, Float rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.rating = rating;
    }

    public Integer getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public LocalDate getReleaseDate() {
        return releaseDate;
    }

    public Float getRating() {
        return rating;
    }
}
