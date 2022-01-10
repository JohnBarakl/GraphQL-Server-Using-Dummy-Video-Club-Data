package videoclub.graphql.server.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.Category;
import videoclub.graphql.server.domain.videoclub.MovieTitle;

import java.sql.SQLException;

/**
 * This class contains methods to resolve field queries of the type Category that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class CategoryFieldsResolver implements GraphQLResolver<Category> {
    /**
     * Acts as resolver for the moviesInCategory field of Category type.
     * @param category The {@link Category} that contains the requested movie titles.
     * @return The MovieTitle list requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public MovieTitle[] moviesInCategory(Category category) throws SQLException {
        return Application.dataSource.retrieveMovieTitlesOfCategory(category);
    }
}
