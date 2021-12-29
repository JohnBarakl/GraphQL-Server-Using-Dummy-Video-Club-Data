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
    // TODO: change releaseDate type to Date when implemented.
    LocalDate releaseDate; // The movie's release date.
    Director[] director; // The movie's director(s).
    Actor[] actor; // The movie's actor(s).
    Category[] category; // The category(-ies) that characterize the movie.
    ProductionCompany[] producedBy; // The production company(-ies) that produce the movie.
    Float rating; // The movie's rating.

    /**
     * Builds a MovieTitle object and sets its fields according to the arguments.
     * @param id The movie's unique identifier.
     * @param title The movie's title.
     * @param description The movie's description.
     * @param releaseDate The movie's release date.
     * @param director The movie's director(s).
     * @param actor The movie's actor(s).
     * @param category The category(-ies) that characterize the movie.
     * @param producedBy The production company(-ies) that produce the movie.
     * @param rating The movie's rating.
     */
    public MovieTitle(Integer id, String title, String description, LocalDate releaseDate, Director[] director, Actor[] actor, Category[] category, ProductionCompany[] producedBy, Float rating) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.releaseDate = releaseDate;
        this.director = director;
        this.actor = actor;
        this.category = category;
        this.producedBy = producedBy;
        this.rating = rating;
    }
}
