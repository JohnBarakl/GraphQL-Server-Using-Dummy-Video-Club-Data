package videoclub.datastore.DataPoints;

import videoclub.datastore.DataIntersection;
import videoclub.graphql.server.domain.videoclub.MovieTitle;
import videoclub.graphql.server.domain.videoclub.ProductionCompany;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

/**
 * The class that manages the application's data concerning the Production Companies. <br>
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class ProductionCompanyData {
    private final Statement queryEndpoint; // The object used for executing a static SQL statements.
    private final DataIntersection dataIntersection; // The object that uses this as an interface to other data.

    /**
     * Sets the Statement object as the "endpoint" to interface with the database through queries.
     * @param queryEndpoint The object used for executing a static SQL statements.
     */
    public ProductionCompanyData(Statement queryEndpoint, DataIntersection dataIntersection) {
        this.queryEndpoint = queryEndpoint;
        this.dataIntersection = dataIntersection;
    }

    /**
     * Executes the given query and returns the list of ProductionCompany object created.
     * @param query The sql query to be executed.
     * @return The list of ProductionCompany objects retrieved.
     */
    private ProductionCompany[] executeProductionCompanyRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        ArrayList<ProductionCompany> productionCompanies = new ArrayList<>();
        synchronized (queryEndpoint) {
            qResults = queryEndpoint.executeQuery(query.toString());

            // Running through the results and constructing Customer objects with the returned data.
            while (qResults.next()) {
                productionCompanies.add(new ProductionCompany(qResults.getInt(1), qResults.getString(2)));
            }
        }

        return productionCompanies.toArray(new ProductionCompany[0]);
    }

    /**
     * Retrieves a number of production companies from the database.
     * @param productionCompany The production company template that will be used for selection of the actors loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of ProductionCompany entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public ProductionCompany[] retrieveProductionCompanies(ProductionCompany productionCompany) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select ProductionCompany.id, ProductionCompany.name ")
                .append("from ProductionCompany ");

        if (productionCompany.getId() == null){ // id not provided
            if (productionCompany.getName() != null){ // Only name is provided.
                sqlQuery.append(String.format("where name = '%s'", productionCompany.getName()));
            }
        } else { // id provided.
            if (productionCompany.getName() == null){ // Only id is provided.
                sqlQuery.append(String.format("where id = '%d'", productionCompany.getId()));
            } else { // Both id and name provided.
                sqlQuery.append(String.format("where id = '%d' and name = '%s'",
                        productionCompany.getId(), productionCompany.getName()));
            }
        }

        return executeProductionCompanyRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Retrieves the list of registered production companies of the movie title given.
     * @param movieTitle The Movie entity which is referenced.
     * @return A list of production companies.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the MovieTitle object fields.
     */
    public ProductionCompany[] retrieveProductionCompanyOfMovieTitle(MovieTitle movieTitle) throws SQLException, IllegalArgumentException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select ProductionCompany.id, ProductionCompany.name ")
                .append("from ProductionCompany inner join producedBy on producedBy.ProductionCompany_id = ProductionCompany.id ")
                .append("where producedBy.MovieTitle_id = ").append(movieTitle.getId());


        // A movie is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieTitle.getId() == null){
            throw new IllegalArgumentException("The MovieTitle must not be null.");
        }

        return executeProductionCompanyRetrievalQuery(sqlQuery.toString());
    }
}
