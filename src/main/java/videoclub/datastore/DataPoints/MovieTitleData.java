package videoclub.datastore.DataPoints;

import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.*;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;

/**
 * The class that manages the application's data concerning the Movie Titles. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class MovieTitleData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public MovieTitleData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Executes the given query and returns the list of movie title object created.
     * @param query The sql query to be executed.
     * @return The list of MovieTitle objects retrieved.
     */
    private MovieTitle[] executeMovieTitleRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<MovieTitle> movieTitles = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query.toString());

            // Running through the results and constructing Customer objects with the returned data.
            while (qResults.next()) {
                movieTitles.add(new MovieTitle(qResults.getInt(1), qResults.getString(2),
                        qResults.getString(3), LocalDate.parse(qResults.getString(4)),
                        qResults.getFloat(5)));
            }
        }

        return movieTitles.toArray(new MovieTitle[0]);
    }

    /**
     * Retrieves a number of movie titles from the database.
     * @param movieTitle The movieTitle template that will be used for selection of the titles loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of MovieTitle entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public MovieTitle[] retrieveMovieTitles(MovieTitle movieTitle) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, title, description, releaseDate, rating ")
                .append("from MovieTitle ");

        // Controls whether the final query will retrieve all movieTitles or will filter the results first.
        boolean getAll = true;

        // Check if it has been given an id filter.
        if (movieTitle.getId() != null){
            getAll = false;
            sqlQuery.append("where id = ").append(movieTitle.getId());
        }

        // Check if it has been given a title filter.
        if (movieTitle.getTitle() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" title = \"").append(movieTitle.getTitle()).append("\"");
        }

        // Check if it has been given a description filter.
        if (movieTitle.getDescription() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" description = \"").append(movieTitle.getDescription()).append("\"");
        }

        // Check if it has been given a releaseDate filter.
        if (movieTitle.getReleaseDate() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" releaseDate = \"").append(movieTitle.getReleaseDate()).append("\"");
        }

        // Check if it has been given a rating filter.
        if (movieTitle.getRating() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" rating = ").append(movieTitle.getRating());
        }

        return executeMovieTitleRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Retrieves the list of registered movies that this director directed.
     * @param director The Director entity which is referenced.
     * @return A list of MovieTitles.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the Director object fields.
     */
    public MovieTitle[] retrieveMovieTitlesOfDirector(Director director) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select MovieTitle.id, MovieTitle.title, MovieTitle.description, MovieTitle.releaseDate, MovieTitle.rating ")
                .append("from MovieTitle inner join MovieTitleParticipants on MovieTitleParticipants.MovieTitle_id = MovieTitle.id ")
                .append("where MovieTitleParticipants.Person_id = ").append(director.getId())
                .append(" and MovieTitleParticipants.participationRole = \"Director\"");

        // An actor object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (director.getId() == null){
            throw new IllegalArgumentException("The ActorId must not be null.");
        }

        return executeMovieTitleRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Retrieves the list of registered movies that this actor starred in.
     * @param actor The Actor entity which is referenced.
     * @return A list of MovieTitles.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the Actor object fields.
     */
    public MovieTitle[] retrieveMovieTitlesOfActor(Actor actor) throws SQLException {
        String sqlQuery = "select MovieTitle.id, MovieTitle.title, MovieTitle.description, MovieTitle.releaseDate, MovieTitle.rating " +
                "from MovieTitle inner join MovieTitleParticipants on MovieTitleParticipants.MovieTitle_id = MovieTitle.id " +
                "where MovieTitleParticipants.Person_id = " + actor.getId() +
                " and MovieTitleParticipants.participationRole = \"Actor\"";

        // An actor object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (actor.getId() == null){
            throw new IllegalArgumentException("The ActorId must not be null.");
        }

        return executeMovieTitleRetrievalQuery(sqlQuery);
    }

    /**
     * Retrieves the list of movies in the category.
     * @param category The Category entity which is referenced.
     * @return A list of MovieTitle entities that are in this Category.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the Category object fields.
     */
    public MovieTitle[] retrieveMovieTitlesOfCategory(Category category) throws SQLException, IllegalArgumentException {

        // A category is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (category.getId() == null){
            throw new IllegalArgumentException("The CategoryId must not be null.");
        }

        String sqlQuery = String.format("select MovieTitle.id, MovieTitle.title, MovieTitle.description, MovieTitle.releaseDate, MovieTitle.rating " +
                "from MovieTitle inner join inCategory on MovieTitle.id = inCategory.MovieTitle_id " +
                "where inCategory.Category_id = %dataSource", category.getId());

        return executeMovieTitleRetrievalQuery(sqlQuery);
    }

    /**
     * Retrieves the list of registered movies that this production company produced.
     * @param productionCompany The Production Company entity which is referenced.
     * @return A list of MovieTitles.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the ProductionCompany object fields.
     */
    public MovieTitle[] retrieveMovieTitlesOfProductionCompany(ProductionCompany productionCompany) throws SQLException, IllegalArgumentException {

        // An actor object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (productionCompany.getId() == null){
            throw new IllegalArgumentException("The ProductionCompany must not be null.");
        }

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select MovieTitle.id, MovieTitle.title, MovieTitle.description, MovieTitle.releaseDate, MovieTitle.rating ")
                .append("from MovieTitle inner join producedBy on producedBy.MovieTitle_id = MovieTitle.id ")
                .append("where producedBy.ProductionCompany_id = ").append(productionCompany.getId());


        return executeMovieTitleRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Retrieves the list of movies in the category.
     * @param movieCopy The Category entity which is referenced.
     * @return A list of MovieTitle entities that are in this Category.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the Category object fields.
     */
    public MovieTitle retrieveMovieTitleOfMovieCopy(MovieCopy movieCopy) throws SQLException, IllegalArgumentException {

        // A category is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieCopy.getId() == null){
            throw new IllegalArgumentException("The MovieCopyId must not be null.");
        }

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, title, description, releaseDate, rating ")
                .append("from MovieTitle ")
                .append("where id = (select MovieTitle_id from MovieCopy where id = ").append(movieCopy.getId()).append(")");

        return executeMovieTitleRetrievalQuery(sqlQuery.toString())[0];
    }

    /**
     * Retrieves a number of movie titles from the database and filters them with the given arguments before returning
     * the list with them.
     * @param title the movie's title.
     * @param releaseDate the movie's release date.
     * @param ratingFrom the lower limit of the movie's rating. If null, the lower limit is considered unbound.
     * @param ratingTo the upper limit of the movie's rating. If null, the upper limit is considered unbound.
     * @return A list with the filtered query result of movie titles.
     * @throws SQLException  If a database access error occurs.
     */
    public MovieTitle[] retrieveMovieTitlesWithFiltering(String title, LocalDate releaseDate, Float ratingFrom, Float ratingTo) throws SQLException {
        // TODO: Look again for second check!

        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, title, description, releaseDate, rating ")
                .append("from MovieTitle ");

        // Controls whether the final query will retrieve all movieTitles or will filter the results first.
        boolean getAll = true;

        // Check if it has been given a title filter.
        if (title != null){
            // If it has another filter to chain into where condition.
            getAll = false;

            sqlQuery.append("where ");

            sqlQuery.append(" title = \"").append(title).append("\"");
        }

        // Check if it has been given a releaseDate filter.
        if (releaseDate != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" releaseDate = \"").append(releaseDate.toString()).append("\"");
        }

        // If ratingFrom is null, no lower limit is imposed.
        if (ratingFrom == null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            if (ratingTo != null){
                sqlQuery.append(" MovieTitle.rating <= ").append(ratingTo);
            }
            // If also upper limit is null, all movie copies are returned: No change to SQL query.
        } else { // There is a lower limit.
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" MovieTitle.rating >= ").append(ratingFrom);
            if (ratingTo != null){ // If there is an upper limit.
                sqlQuery.append(" and MovieTitle.rating <= ").append(ratingTo);
            }
        }


        return executeMovieTitleRetrievalQuery(sqlQuery.toString());
    }
}
