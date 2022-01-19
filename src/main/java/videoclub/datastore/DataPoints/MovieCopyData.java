package videoclub.datastore.DataPoints;

import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.Customer;
import videoclub.graphql.server.domain.videoclub.MovieCopy;
import videoclub.graphql.server.domain.videoclub.MovieTitle;
import videoclub.graphql.server.domain.videoclub.RentTransaction;
import videoclub.graphql.server.domain.videoclub.input.DeleteMovieCopyInput;
import videoclub.graphql.server.domain.videoclub.input.NewMovieCopyInput;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class that manages the application's data concerning the Movie Copies. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class MovieCopyData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public MovieCopyData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Executes the given query and returns the list of MovieCopy object created.
     * WARNING: The MovieCopy and MovieFormat must be requested as strings and not ids.
     *
     * @param query The sql query to be executed.
     * @param ignoreDeleted if true, the rows that have the column value deleted as 1, will not be returned.
     * @return The list of MovieCopy objects retrieved.
     */
    private MovieCopy[] executeMovieCopyRetrievalQuery(String query, boolean ignoreDeleted) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<MovieCopy> movieCopies = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query.toString());

            // Running through the results and constructing Customer objects with the returned data.
            while (qResults.next()) {
                // Ignore this row if it is marked as deleted and these kinds of objects are ignored.
                if (qResults.getBoolean(5) && ignoreDeleted)
                    continue;

                movieCopies.add(new MovieCopy(qResults.getInt(1), MovieCopy.Medium.valueOf(qResults.getString(2)), MovieCopy.MovieFormat.valueOf(qResults.getString(3)),
                        qResults.getFloat(4)));
            }
        }

        return movieCopies.toArray(new MovieCopy[0]);
    }

    /**
     * Retrieves the movie copy involved in the transaction of the argument.
     * @param rentTransaction The rent transaction that involves a customer.
     * @return A MovieCopy entity.
     * @throws SQLException If a database access error occurs.
     */
    public MovieCopy retrieveMovieCopyFromTransaction(RentTransaction rentTransaction) throws SQLException {
        // A rent transaction object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (rentTransaction.getId() == null){
            throw new IllegalArgumentException("The RentTransactionId must not be null.");
        }

        String sqlQuery = "select MovieCopy.id, Medium.name as \"MediumName\", MovieFormat.name as \"FormatName\", rentPrice, deleted " +
                "from MovieCopy inner join Medium on Medium_id = Medium.id " +
                "               inner join MovieFormat on MovieFormat_id = MovieFormat.id " +
                "where MovieCopy.id = (select MovieCopy_id from RentTransaction where id = " + rentTransaction.getId() + " )";

        return executeMovieCopyRetrievalQuery(sqlQuery, false)[0];
    }

    /**
     * Retrieves a number of movie copies that match the template given that are not flagged as deleted.
     * @param movieCopy The MovieCopy template that will be used for selection of the movie copies loaded from the database.
     *                  <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of MovieCopy entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public MovieCopy[] retrieveMovieCopy(MovieCopy movieCopy) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select MovieCopy.id, Medium.name as \"MediumName\", MovieFormat.name as \"FormatName\", rentPrice, deleted ")
                .append("from MovieCopy inner join Medium on Medium_id = Medium.id ")
                .append("               inner join MovieFormat on MovieFormat_id = MovieFormat.id ");

        // Controls whether the final query will retrieve all movie copies or will filter the results first.
        boolean getAll = true;

        // Check if it has been given an id filter.
        if (movieCopy.getId() != null){
            getAll = false;
            sqlQuery.append("where MovieCopy.id = ").append(movieCopy.getId());
        }

        // Check if it has been given a medium filter.
        if (movieCopy.getMedium() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" MediumName = \"").append(movieCopy.getMedium().toString()).append("\"");
        }

        // Check if it has been given a copy type filter.
        if (movieCopy.getCopyType() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" FormatName = \"").append(movieCopy.getCopyType().toString()).append("\"");
        }

        // Check if it has been given a rentPrice filter.
        if (movieCopy.getRentPrice() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" rentPrice = ").append(movieCopy.getRentPrice());
        }

        return executeMovieCopyRetrievalQuery(sqlQuery.toString(), true);
    }

    /**
     * Retrieves a number of movie copies that have a rent price inside the given price limits.
     * If one (or both) the upper or lower limit is null, then the price, regarding that end of the range, is considered unbound.
     * @param priceFrom the price lower limit.
     * @param priceTo the price upper limit.
     * @return A list of MovieCopy entities that have a rent price inside the given limits.
     * @throws SQLException If a database access error occurs.
     */
    public MovieCopy[] retrieveMovieCopiesInPriceRange(Float priceFrom, Float priceTo) throws SQLException {
        String sqlQuery = "select MovieCopy.id, Medium.name as \"MediumName\", MovieFormat.name as \"FormatName\", rentPrice, deleted " +
                          "from MovieCopy inner join Medium on Medium_id = Medium.id " +
                          "               inner join MovieFormat on MovieFormat_id = MovieFormat.id ";

        // If priceFrom is null, no lower limit is imposed.
        if (priceFrom == null){
             if (priceTo != null){
                 sqlQuery += "where MovieCopy.rentPrice <= " + priceTo;
             }
            // If also upper limit is null, all movie copies are returned: No change to SQL query.
        } else { // There is a lower limit.
            sqlQuery += "where MovieCopy.rentPrice >= " + priceFrom;
            if (priceTo != null){ // If there is an upper limit.
                sqlQuery += " and MovieCopy.rentPrice <= " + priceTo;
            }
        }

        return executeMovieCopyRetrievalQuery(sqlQuery, false);
    }

    /**
     * Retrieves the list of movie copies that contain this movie title.
     * @param movieTitle The movie title.
     * @return A list of MovieCopy entities.
     * @throws SQLException If a database access error occurs.
     */
    public MovieCopy[] retrieveMovieCopiesOfMovieTitle(MovieTitle movieTitle) throws SQLException {
        // A movie title object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieTitle.getId() == null){
            throw new IllegalArgumentException("The MovieTitleId must not be null.");
        }

        String sqlQuery = "select MovieCopy.id, Medium.name as \"MediumName\", MovieFormat.name as \"FormatName\", rentPrice, deleted " +
                "from MovieCopy inner join Medium on Medium_id = Medium.id " +
                "               inner join MovieFormat on MovieFormat_id = MovieFormat.id " +
                "where MovieCopy.MovieTitle_id = " + movieTitle.getId();

        return executeMovieCopyRetrievalQuery(sqlQuery, false);
    }

    /**
     * Creates a new movie copy in the database and returns the corresponding object.
     * @param input The input object.
     * @return the new movie copy just created and inserted in the database.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public MovieCopy insertNewMovieCopy(NewMovieCopyInput input) throws SQLException {
        StringBuilder columns = new StringBuilder();
        StringBuilder values = new StringBuilder();

        // Check if movie id was provided.
        if (input.getMovieTitleId() == null){
            throw new IllegalArgumentException("MovieTitleId required.");
        } else {
            columns.append("MovieTitle_id, ");
            values.append("'").append(input.getMovieTitleId()).append("', ");
        }

        // Check if medium was provided.
        if (input.getMedium() == null) {
            throw new IllegalArgumentException("Medium required.");
        } else {
            int mediumId;
            synchronized (queryEndpoint){ // "Lock" the database.
                ResultSet rs = queryEndpoint.executeQuery( // Lookup of the id of the Medium in the database.
                        "select id from Medium where name = '" + input.getMedium().toString() + "'"
                );

                // Assuming the database returned one and only row (medium names should be unique), extract the required id.
                mediumId = rs.getInt(1);
            }

            columns.append("Medium_id, ");
            values.append("'").append(mediumId).append("', ");
        }

        // Check if copyType was provided.
        if (input.getCopyType() == null){
            throw new IllegalArgumentException("CopyType required.");
        } else {
            int movieFormatId;
            synchronized (queryEndpoint){ // "Lock" the database.
                ResultSet rs = queryEndpoint.executeQuery( // Lookup of the id of the Format in the database.
                        "select id from MovieFormat where name == '" + input.getCopyType().toString() + "'"
                );

                // Assuming the database returned one and only row (format names should be unique), extract the required id.
                movieFormatId = rs.getInt(1);
            }

            columns.append("MovieFormat_id, ");
            values.append("'").append(movieFormatId).append("', ");
        }

        // Check rentPrice was provided.
        if (input.getRentPrice() == null){
            throw new IllegalArgumentException("Rent price required.");
        } else {
            columns.append("rentPrice, ");
            values.append("'").append(input.getRentPrice()).append("', ");
        }

        columns.replace(columns.length() - 2, columns.length(), "");
        values.replace(values.length() - 2, values.length(), "");


        String query = String.format("insert into MovieCopy ( %s ) %n" +
                "values ( %s )", columns.toString(), values.toString());

        int newMovieCopyId;

        // Making sure there are no synchronization errors.
        synchronized (queryEndpoint) {
            queryEndpoint.execute(query);

            // Query the database for all the valid ids. The one with the greatest value is the one just inserted.
            String newIdQuery = "select id " +
                    "from MovieCopy " +
                    "order by id desc limit 1";

            ResultSet qResults = queryEndpoint.executeQuery(newIdQuery);

            newMovieCopyId = qResults.getInt(1);
        }
        // Searching and returning the new customer object by id.
        // Only one is expected to be returned.
        return retrieveMovieCopy(new MovieCopy(newMovieCopyId, null, null, null))[0];
    }

    /**
     * Flags a  movie copy as deleted in the database and returns the operation's success state.
     * @param input The input object.
     * @return the operation's success state.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException if one or more required fields are missing.
     */
    public boolean deleteMovieCopy(DeleteMovieCopyInput input) throws SQLException {
        int id;

        // Check if movie copy id was provided.
        if (input.getMovieCopyID() == null){
            throw new IllegalArgumentException("MovieCopyId required.");
        } else {
            id = input.getMovieCopyID();
        }

        int result;

        // Making sure there are no synchronization errors.
        synchronized (queryEndpoint) {
            // Mark deleted = 1 to the movie copy we want deleted.
            queryEndpoint.execute("update MovieCopy " +
                                     "set deleted = 1 " +
                                     "where id = " + id);

            result = queryEndpoint.getUpdateCount();
        }
        // The query should change exactly one row.
        // In any other case, it failed.
        return result == 1;
    }
}
