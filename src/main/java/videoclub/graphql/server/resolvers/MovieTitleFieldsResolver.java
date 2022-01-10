package videoclub.graphql.server.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.*;

import java.sql.SQLException;

/**
 * This class contains methods to resolve field queries of the type MovieTitle that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class MovieTitleFieldsResolver implements GraphQLResolver<MovieTitle> {


    /**
     * Acts as resolver for the directors field of MovieTitle type.
     * @param movieTitle The {@link MovieTitle} that contains the requested directors.
     * @return The directors requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public Director[] directors(MovieTitle movieTitle) throws SQLException {
        return Application.dataSource.retrieveDirectorsOfMovieTitle(movieTitle);
    }

    /**
     * Acts as resolver for the actors field of MovieTitle type.
     * @param movieTitle The {@link MovieTitle} that contains the requested actors.
     * @return The actors requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public Actor[] actors(MovieTitle movieTitle) throws SQLException {
        return Application.dataSource.retrieveActorsOfMovieTitle(movieTitle);
    }

    /**
     * Acts as resolver for the inCategory field of MovieTitle type.
     * @param movieTitle The {@link MovieTitle} that contains the requested categories.
     * @return The categories requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public Category[] inCategory(MovieTitle movieTitle) throws SQLException {
        return Application.dataSource.retrieveCategoriesOfMovieTitle(movieTitle);
    }

    /**
     * Acts as resolver for the producedBy field of MovieTitle type.
     * @param movieTitle The {@link MovieTitle} that contains the requested production companies.
     * @return The production companies requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public ProductionCompany[] producedBy(MovieTitle movieTitle) throws SQLException {
        return Application.dataSource.retrieveProductionCompanyOfMovieTitle(movieTitle);
    }
}
