package videoclub.datastore.DataPoints;

import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.Director;
import videoclub.graphql.server.domain.videoclub.MovieTitle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;


/**
 * The class that manages the application's data concerning the Directors. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class DirectorData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public DirectorData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Executes the given query and returns the list of director object created.
     * @param query The sql query to be executed.
     * @return The list of director objects retrieved.
     */
    private Director[] executeDirectorRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<Director> directors = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query);


            // Running through the results and constructing Actor objects with the returned data.
            while (qResults.next()) {
                directors.add(new Director(qResults.getInt(1), qResults.getString(2)));
            }
        }

        return directors.toArray(new Director[0]);
    }

    /**
     * Retrieves a number of directors from the database.
     * @param director The actor template that will be used for selection of the directors loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of Director entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public Director[] retrieveDirectors(Director director) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select Person.id, Person.name ")
                .append("from Person inner join MovieTitleParticipants on Person.id = MovieTitleParticipants.Person_id ")
                .append("where MovieTitleParticipants.participationRole = \"Director\" ");

        if (director.getId() != null){ // id is provided
            sqlQuery.append(String.format("and id = %dataSource ", director.getId()));
        }

        if (director.getName() != null){ // Actor's name is provided.
            sqlQuery.append(String.format("and name = \"%s\" ", director.getName()));
        }


        return executeDirectorRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Retrieves the list of registered directors that directed the movie given.
     * @param movieTitle The Movie entity which is referenced.
     * @return A list of Directors directing the movie.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the Director object fields.
     */
    public Director[] retrieveDirectorsOfMovieTitle(MovieTitle movieTitle) throws SQLException {
        // A movie is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieTitle.getId() == null){
            throw new IllegalArgumentException("The DirectorId must not be null.");
        }

        String sqlQuery = "select Person.id, Person.name " +
                "from Person inner join MovieTitleParticipants on MovieTitleParticipants.Person_id = MovieTitle.id " +
                "where MovieTitleParticipants.MovieTitle_id = " + movieTitle.getId() +
                " and MovieTitleParticipants.participationRole = \"Director\""; // We want the directors of this movie. An actor may also be a director.

        return executeDirectorRetrievalQuery(sqlQuery);
    }

}
