package videoclub.datastore;

import videoclub.datastore.DataPoints.*;
import videoclub.graphql.server.domain.videoclub.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * The class that manages the application's data. <br>
 *
 * It provides methods for objects that handle I/O with the database.
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class DataIntersection {
    private Connection dbConnection; // The connection to the SQLite database.

    private Statement queryEndpoint;

    // The objects that this class ties together for the sake of organization
    private final ActorData actorIO;
    private final CategoryData categoryIO;
    private final CustomerData customerIO;
    private final DirectorData directorIO;
    private final MovieCopyData movieCopyIO;
    private final MovieTitleData movieTitleIO;
    private final ProductionCompanyData productionCompanyIO;
    private final RentTransactionData rentTransactionIO;

    /**
     * Creates a DataIntersection object, establishing a connection with the database located in the argument
     * filepath and initializes the objects handling the IO regarding certain entities.
     *
     * @param db_filepath Filepath of the SQLite database file.
     * @throws SQLException if a database access error occurs or the url is null.
     */
    public DataIntersection(String db_filepath) throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:" + db_filepath); // Creating connection to the database with the given filepath.
        queryEndpoint = dbConnection.createStatement();
        actorIO = new ActorData(queryEndpoint, this);
        categoryIO = new CategoryData(queryEndpoint, this);
        customerIO = new CustomerData(queryEndpoint, this);
        directorIO = new DirectorData(queryEndpoint, this);
        movieCopyIO = new MovieCopyData(queryEndpoint, this);
        movieTitleIO = new MovieTitleData(queryEndpoint, this);
        productionCompanyIO = new ProductionCompanyData(queryEndpoint, this);
        rentTransactionIO = new RentTransactionData(queryEndpoint, this);
    }

    /**
     * @return The interface class that handles the Actor data.
     */
    public ActorData aboutActors() {
        return actorIO;
    }

    /**
     * @return The interface class that handles the Category data.
     */
    public CategoryData aboutCategories() {
        return categoryIO;
    }

    /**
     * @return The interface class that handles the Customer data.
     */
    public CustomerData aboutCustomers() {
        return customerIO;
    }

    /**
     * @return The interface class that handles the Director data.
     */
    public DirectorData aboutDirectors() {
        return directorIO;
    }

    /**
     * @return The interface class that handles the Movie Copy data.
     */
    public MovieCopyData aboutMovieCopies() {
        return movieCopyIO;
    }

    /**
     * @return The interface class that handles the MovieTitle data.
     */
    public MovieTitleData aboutMovieTitles() {
        return movieTitleIO;
    }

    /**
     * @return The interface class that handles the Production Company data.
     */
    public ProductionCompanyData aboutProductionCompanies() {
        return productionCompanyIO;
    }

    /**
     * @return The interface class that handles the Rent Transaction data.
     */
    public RentTransactionData aboutRentTransactions() {
        return rentTransactionIO;
    }

}
