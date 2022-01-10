package videoclub.graphql.server.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.MovieCopy;
import videoclub.graphql.server.domain.videoclub.MovieTitle;

import java.sql.SQLException;

/**
 * This class contains methods to resolve field queries of the type MovieCopy that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class MovieCopyFieldsResolver implements GraphQLResolver<MovieCopy> {

    /**
     * Acts as resolver for the movieTitle field of MovieCopy type.
     * @param movieCopy The {@link MovieCopy} that contains the requested movie title.
     * @return The movie title requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public MovieTitle movieTitle(MovieCopy movieCopy) throws SQLException {
        return Application.dataSource.retrieveMovieTitleOfMovieCopy(movieCopy);
    }
}
