package videoclub.datastore.DataPoints;

import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.Category;
import videoclub.graphql.server.domain.videoclub.MovieTitle;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class that manages the application's data concerning the Categories. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class CategoryData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public CategoryData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Executes the given query and returns the list of category object created.
     * @param query The sql query to be executed.
     * @return The list of category objects retrieved.
     */
    private Category[] executeCategoryRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<Category> categories = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query);

            // Running through the results and constructing Category objects with the returned data.
            while (qResults.next()) {
                categories.add(new Category(qResults.getInt(1), qResults.getString(2)));
            }
        }

        return categories.toArray(new Category[0]);
    }

    /**
     * Retrieves a number of categories from the database.
     * @param category The category template that will be used for selection of the categories loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of Category entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public Category[] retrieveCategories(Category category) throws SQLException {
        String sqlQuery = "select Category.id, Category.name " +
                "from  Category ";

        if (category.getId() == null){ // id not provided
            if (category.getName() != null){ // Only name is provided.
                sqlQuery += String.format("where name = '%s'", category.getName());
            }
        } else { // id provided.
            if (category.getName() == null){ // Only id is provided.
                sqlQuery += String.format("where id = '%dataSource'", category.getId());
            } else { // Both id and name provided.
                sqlQuery += String.format("where id = '%dataSource' and name = '%s'", category.getId(), category.getName());
            }
        }

        return executeCategoryRetrievalQuery(sqlQuery);
    }

    /**
     * Retrieves the list of categories of the movie.
     * @param movieTitle The movieTitle entity which is referenced.
     * @return A list of Category entities that are connected to the movie.
     * @throws IllegalArgumentException If there is an error in the movieTitle object fields.
     * @throws SQLException If a database access error occurs.
     */
    public Category[] retrieveCategoriesOfMovieTitle(MovieTitle movieTitle) throws SQLException, IllegalArgumentException {
        // A movie is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieTitle.getId() == null){
            throw new IllegalArgumentException("The MovieTitleId must not be null.");
        }

        String sqlQuery = String.format("select Category.id, Category.name " +
                "from Category inner join inCategory on Category.id = inCategory.Category_id " +
                "where inCategory.MovieTitle_id = %dataSource", movieTitle.getId());

        return executeCategoryRetrievalQuery(sqlQuery);
    }

}
