package videoclub.graphql.server.resolvers.fields;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.Director;
import videoclub.graphql.server.domain.videoclub.MovieTitle;

import java.sql.SQLException;

/**
 * This class contains methods to resolve field queries of the type Director that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class DirectorFieldsResolver implements GraphQLResolver<Director> {
    /**
     * Acts as resolver for the moviesDirected field of Director type.
     * @param director The {@link Director} specified.
     * @return The MovieTitle list requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public MovieTitle[] moviesDirected(Director director) throws SQLException {
        return Application.dataSource.aboutMovieTitles().retrieveMovieTitlesOfDirector(director);
    }
}
