package videoclub.graphql.server.resolvers.fields;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.Actor;
import videoclub.graphql.server.domain.videoclub.MovieTitle;

import java.sql.SQLException;

/**
 * This class contains methods to resolve field queries of the type Actor that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class ActorFieldsResolver implements GraphQLResolver<Actor> {
    /**
     * Acts as resolver for the moviesPlayedIn field of Actor type.
     * @param actor The {@link Actor} specified.
     * @return The MovieTitle list requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public MovieTitle[] moviesPlayedIn(Actor actor) throws SQLException {
        return Application.dataSource.aboutMovieTitles().retrieveMovieTitlesOfActor(actor);
    }
}
