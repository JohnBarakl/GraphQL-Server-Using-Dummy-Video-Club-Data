package videoclub.datastore.DataPoints;

import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.Actor;
import videoclub.graphql.server.domain.videoclub.MovieTitle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class that manages the application's data concerning the Actors. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class ActorData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public ActorData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Retrieves a number of actors from the database.
     * @param actor The actor template that will be used for selection of the actors loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of Actor entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public Actor[] retrieveActors(Actor actor) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select distinct Person.id, Person.name ")
                .append("from Person inner join MovieTitleParticipants on Person.id = MovieTitleParticipants.Person_id ")
                .append("where MovieTitleParticipants.participationRole = \"Actor\" ");

        if (actor.getId() != null){ // id is provided
            sqlQuery.append(String.format("and id = %d ", actor.getId()));
        }

        if (actor.getName() != null){ // Actor's name is provided.
            sqlQuery.append(String.format("and name = \"%s\" ", actor.getName()));
        }

        return executeActorRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Executes the given query and returns the list of actor object created.
     * @param query The sql query to be executed.
     * @return The list of actor objects retrieved.
     */
    private Actor[] executeActorRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<Actor> actors = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query);


            // Running through the results and constructing Actor objects with the returned data.
            while (qResults.next()) {
                actors.add(new Actor(qResults.getInt(1), qResults.getString(2)));
            }
        }

        return actors.toArray(new Actor[0]);
    }

    /**
     * Retrieves the list of registered actors that starred in the movie title given.
     * @param movieTitle The Movie entity which is referenced.
     * @return A list of Actors starring in the movie.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the MovieTitle object fields.
     */
    public Actor[] retrieveActorsOfMovieTitle(MovieTitle movieTitle) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select distinct Person.id, Person.name ")
                .append("from Person inner join MovieTitleParticipants on MovieTitleParticipants.Person_id = Person.id ")
                .append("where MovieTitleParticipants.MovieTitle_id = ").append(movieTitle.getId())
                .append(" and MovieTitleParticipants.participationRole = \"Actor\""); // We want the actors of this movie. An actor may also be a director.


        // A movie is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieTitle.getId() == null){
            throw new IllegalArgumentException("The MovieTitleId must not be null.");
        }

        return executeActorRetrievalQuery(sqlQuery.toString());
    }

}
