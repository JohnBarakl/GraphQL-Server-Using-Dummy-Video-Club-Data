package videoclub.datastore;

import videoclub.graphql.server.domain.videoclub.*;

import java.sql.*;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.util.ArrayList;

/**
 * The class that manages the application's data. <br>
 *
 * It provides methods for I/O with the database.
 *
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class Data {
    private Connection dbConnection;

    private Statement queryEndpoint;


    /**
     * Creates a Data object, establishing a connection with the database located in the argument
     * filepath.
     *
     * @param db_filepath Filepath of the SQLite database file.
     * @throws SQLException if a database access error occurs or the url is null.
     */
    public Data(String db_filepath) throws SQLException {
        dbConnection = DriverManager.getConnection("jdbc:sqlite:" + db_filepath);
        queryEndpoint = dbConnection.createStatement();
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
        sqlQuery.append("select Person.id, Person.name ")
                .append("from Person inner join MovieTitleParticipants on Person.id = MovieTitleParticipants.Person_id ")
                .append("where MovieTitleParticipants.participationRole = \"Actor\" ");

        if (actor.getId() != null){ // id is provided
            sqlQuery.append(String.format("and id = %dataSource ", actor.getId()));
        }

        if (actor.getName() != null){ // Actor's name is provided.
            sqlQuery.append(String.format("and name = \"%s\" ", actor.getName()));
        }

        return executeActorRetrievalQuery(sqlQuery.toString());
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
     * Retrieves a number of customers from the database.
     * @param customer The customer template that will be used for selection of the customers loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of Customer entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public Customer[] retrieveCustomers(Customer customer) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, fullName, dateOfBirth, address, phoneNumber, email ")
                .append("from Customer ");

        // Controls whether the final query will retrieve all customers or will filter the results first.
        boolean getAll = true;

        // Check if it has been given an id filter.
        if (customer.getId() != null){
            getAll = false;
            sqlQuery.append("where id = ").append(customer.getId());
        }

        // Check if it has been given an fullName filter.
        if (customer.getFullName() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" fullName = \"").append(customer.getFullName()).append("\"");
        }

        // Check if it has been given a dateOfBirth filter.
        if (customer.getDateOfBirth() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" dateOfBirth = \"").append(customer.getDateOfBirth()).append("\"");
        }

        // Check if it has been given a phoneNumber filter.
        if (customer.getPhoneNumber() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" phoneNumber = \"").append(customer.getPhoneNumber()).append("\"");
        }

        // Check if it has been given an email filter.
        if (customer.getEmail() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" email = \"").append(customer.getEmail()).append("\"");
        }

        return executeCustomerRetrievalQuery(sqlQuery.toString());
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
     * Retrieves the list of registered actors that starred in the movie title given.
     * @param movieTitle The Movie entity which is referenced.
     * @return A list of Actors starring in the movie.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the MovieTitle object fields.
     */
    public Actor[] retrieveActorsOfMovieTitle(MovieTitle movieTitle) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select Person.id, Person.name ")
                .append("from Person inner join MovieTitleParticipants on MovieTitleParticipants.Person_id = MovieTitle.id ")
                .append("where MovieTitleParticipants.MovieTitle_id = ").append(movieTitle.getId())
                .append(" and MovieTitleParticipants.participationRole = \"Actor\""); // We want the actors of this movie. An actor may also be a director.


        // A movie is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieTitle.getId() == null){
            throw new IllegalArgumentException("The MovieTitleId must not be null.");
        }

        return executeActorRetrievalQuery(sqlQuery.toString());
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
                sqlQuery.append(String.format("where id = '%dataSource'", productionCompany.getId()));
            } else { // Both id and name provided.
                sqlQuery.append(String.format("where id = '%dataSource' and name = '%s'",
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
     * Retrieves a number of rent transactions from the database.
     * @param rentTransaction The rent transaction template that will be used for selection of the actors loaded from the database.
     *              <p>Any object fields that have the value null will be substituted for any value</p>
     *              <p>Note: if the dateTo field has the value OffsetDateTime.MAX, then the results will be filtered so that
     *                       the column dateTo is null.</p>
     * @return A list of RentTransaction entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public RentTransaction[] retrieveRentTransactions(RentTransaction rentTransaction) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select id, price, dateFrom, dateTo ")
                .append("from RentTransaction ");

        // Controls whether the final query will retrieve all rent transactions or will filter the results first.
        boolean getAll = true;

        // Check if it has been given an id filter.
        if (rentTransaction.getId() != null){
            getAll = false;
            sqlQuery.append("where id = ").append(rentTransaction.getId());
        }

        // Check if it has been given a price filter.
        if (rentTransaction.getPrice() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" price = \"").append( rentTransaction.getPrice() ).append("\"");
        }

        // Check if it has been given a dateFrom filter.
        if (rentTransaction.getDateFrom() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ");
            } else {
                sqlQuery.append(" and");
            }

            sqlQuery.append(" dateFrom = \"").append( rentTransaction.getDateFrom() ).append("\"");
        }

        // Check if it has been given a dateTo filter.
        if (rentTransaction.getDateTo() != null){
            if (getAll) { // If it has another filter to chain into where condition.
                getAll = false;
                sqlQuery.append("where ").append( rentTransaction.getDateTo() ).append("\"");
            } else {
                sqlQuery.append(" and");

                // OffsetDateTime.MAX signal value of null
                if (rentTransaction.getDateTo().equals(OffsetDateTime.MAX)){
                    sqlQuery.append(" dateTo is null");
                } else {
                    sqlQuery.append(" dateTo = \"").append( rentTransaction.getDateTo() ).append("\"");
                }
            }
        }

        return executeRentTransactionRetrievalQuery(sqlQuery.toString());
    }

    /**
     * Returns a list of rent transactions that involve a customer.
     * @param customer The customer that is referenced.
     * @return A list of RentTransaction entities.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the ProductionCompany object fields.
     */
    public RentTransaction[] retrieveRentTransactionsFromCustomer(Customer customer) throws SQLException, IllegalArgumentException  {

        // An actor object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (customer.getId() == null){
            throw new IllegalArgumentException("The CustomerId must not be null.");
        }

        String sqlQuery = String.format("select id, price, dateFrom, dateTo " +
                                        "from RentTransaction " +
                                        "where Customer_id = %d", customer.getId());

        return executeRentTransactionRetrievalQuery(sqlQuery);
    }

    /**
     * Returns a list of rent transactions that involve a movie copy.
     * @param movieCopy The movie copy that is referenced.
     * @return A list of RentTransaction entities.
     * @throws SQLException If a database access error occurs.
     * @throws IllegalArgumentException If there is an error in the ProductionCompany object fields.
     */
    public RentTransaction[] retrieveRentTransactionsFromMovieCopy(MovieCopy movieCopy) throws SQLException, IllegalArgumentException  {

        // An actor object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (movieCopy.getId() == null){
            throw new IllegalArgumentException("The MovieCopyId must not be null.");
        }

        String sqlQuery = String.format("select id, price, dateFrom, dateTo " +
                                        "from RentTransaction " +
                                        "where MovieCopy_id = %dataSource", movieCopy.getId());

        return executeRentTransactionRetrievalQuery(sqlQuery);
    }

    /**
     * Retrieves the customer involved in the transaction of the argument.
     * @param rentTransaction The rent transaction that involves a customer.
     * @return A Customer entity.
     * @throws SQLException If a database access error occurs.
     */
    public Customer retrieveCustomerFromTransaction(RentTransaction rentTransaction) throws SQLException {
        // A rent transaction object is uniquely identified by its id. If it is null, then an IllegalArgumentException is thrown.
        if (rentTransaction.getId() == null){
            throw new IllegalArgumentException("The RentTransactionId must not be null.");
        }

        String sqlQuery = "select Customer.id, Customer.fullName, Customer.dateOfBirth, Customer.address, Customer.phoneNumber, Customer.email " +
                          "from Customer " +
                          "where Customer.id = (select Customer_id from RentTransaction where id = " + rentTransaction.getId() + " )";

        return executeCustomerRetrievalQuery(sqlQuery)[0];
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

        String sqlQuery = "select MovieCopy.id, Medium.name as \"MediumName\", MovieFormat.name as \"FormatName\", rentPrice " +
                          "from MovieCopy inner join Medium on Medium_id = Medium.id " +
                          "               inner join MovieFormat on MovieFormat_id = MovieFormat.id " +
                          "where MovieCopy.id = (select MovieCopy_id from RentTransaction where id = " + rentTransaction.getId() + " )";

        return executeMovieCopyRetrievalQuery(sqlQuery)[0];
    }

    /**
     * Retrieves a number of movie copies that match the template given.
     * @param movieCopy The MovieCopy template that will be used for selection of the movie copies loaded from the database.
     *                  <p>Any object fields that have the value null will be substituted for any value</p>
     * @return A list of MovieCopy entities that match the template given as argument.
     * @throws SQLException If a database access error occurs.
     */
    public MovieCopy[] retrieveMovieCopy(MovieCopy movieCopy) throws SQLException {
        StringBuilder sqlQuery = new StringBuilder();
        sqlQuery.append("select MovieCopy.id, Medium.name as \"MediumName\", MovieFormat.name as \"FormatName\", rentPrice ")
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

        return executeMovieCopyRetrievalQuery(sqlQuery.toString());
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

        String sqlQuery = "select MovieCopy.id, Medium.name as \"MediumName\", MovieFormat.name as \"FormatName\", rentPrice " +
                          "from MovieCopy inner join Medium on Medium_id = Medium.id " +
                          "               inner join MovieFormat on MovieFormat_id = MovieFormat.id " +
                          "where MovieCopy.MovieTitle_id = " + movieTitle.getId();

        return executeMovieCopyRetrievalQuery(sqlQuery);
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
     * Executes the given query and returns the list of actor object created.
     * @param query The sql query to be executed.
     * @return The list of actor objects retrieved.
     */
    private Actor[] executeActorRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query);
        }

        ArrayList<Actor> actors = new ArrayList<>();
        // Running through the results and constructing Actor objects with the returned data.
        while (qResults.next()){
            actors.add(new Actor(qResults.getInt(1), qResults.getString(2)));
        }

        return actors.toArray(new Actor[0]);
    }

    /**
     * Executes the given query and returns the list of director object created.
     * @param query The sql query to be executed.
     * @return The list of director objects retrieved.
     */
    private Director[] executeDirectorRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query);
        }

        ArrayList<Director> directors = new ArrayList<>();
        // Running through the results and constructing Actor objects with the returned data.
        while (qResults.next()){
            directors.add(new Director(qResults.getInt(1), qResults.getString(2)));
        }

        return directors.toArray(new Director[0]);
    }

    /**
     * Executes the given query and returns the list of category object created.
     * @param query The sql query to be executed.
     * @return The list of category objects retrieved.
     */
    private Category[] executeCategoryRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query);
        }

        ArrayList<Category> categories = new ArrayList<>();
        // Running through the results and constructing Category objects with the returned data.
        while (qResults.next()){
            categories.add(new Category(qResults.getInt(1), qResults.getString(2)));
        }

        return categories.toArray(new Category[0]);
    }

    /**
     * Executes the given query and returns the list of movie title object created.
     * @param query The sql query to be executed.
     * @return The list of MovieTitle objects retrieved.
     */
    private MovieTitle[] executeMovieTitleRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query.toString());
        }

        ArrayList<MovieTitle> movieTitles = new ArrayList<>();
        // Running through the results and constructing Customer objects with the returned data.
        while (qResults.next()){
            movieTitles.add( new MovieTitle(qResults.getInt(1), qResults.getString(2),
                    qResults.getString(3), LocalDate.parse(qResults.getString(4)),
                    qResults.getFloat(5)));
        }

        return movieTitles.toArray(new MovieTitle[0]);
    }

    /**
     * Executes the given query and returns the list of customer object created.
     * @param query The sql query to be executed.
     * @return The list of Customers objects retrieved.
     */
    private Customer[] executeCustomerRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query.toString());
        }

        ArrayList<Customer> customers = new ArrayList<>();
        // Running through the results and constructing Customer objects with the returned data.
        while (qResults.next()){
            customers.add(new Customer(qResults.getInt(1), qResults.getString(2),
                    LocalDate.parse(qResults.getString(3)), qResults.getString(4),
                    qResults.getString(5), qResults.getString(6)));
        }

        return customers.toArray(new Customer[0]);
    }

    /**
     * Executes the given query and returns the list of ProductionCompany object created.
     * @param query The sql query to be executed.
     * @return The list of ProductionCompany objects retrieved.
     */
    private ProductionCompany[] executeProductionCompanyRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query.toString());
        }

        ArrayList<ProductionCompany> productionCompanies = new ArrayList<>();
        // Running through the results and constructing Customer objects with the returned data.
        while (qResults.next()){
            productionCompanies.add(new ProductionCompany(qResults.getInt(1), qResults.getString(2)));
        }

        return productionCompanies.toArray(new ProductionCompany[0]);
    }

    /**
     * Executes the given query and returns the list of RentTransaction object created.
     * @param query The sql query to be executed.
     * @return The list of RentTransaction objects retrieved.
     */
    private RentTransaction[] executeRentTransactionRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query.toString());
        }

        ArrayList<RentTransaction> rentTransactions = new ArrayList<>();
        // Running through the results and constructing Customer objects with the returned data.
        while (qResults.next()){
            rentTransactions.add(new RentTransaction(qResults.getInt(1), qResults.getFloat(2),
                    OffsetDateTime.parse(qResults.getString(3)),
                    qResults.getString(4)!=null?OffsetDateTime.parse(qResults.getString(4)):null));
        }

        return rentTransactions.toArray(new RentTransaction[0]);
    }

    /**
     * Executes the given query and returns the list of MovieCopy object created.
     * WARNING: The MovieCopy and MovieFormat must be requested as strings and not ids.
     *
     * @param query The sql query to be executed.
     * @return The list of MovieCopy objects retrieved.
     */
    private MovieCopy[] executeMovieCopyRetrievalQuery(String query) throws SQLException {
        // Asserting synchronization of database accesses.
        ResultSet qResults;
        synchronized (this){
            qResults = queryEndpoint.executeQuery(query.toString());
        }

        ArrayList<MovieCopy> movieCopies = new ArrayList<>();
        // Running through the results and constructing Customer objects with the returned data.
        while (qResults.next()){
            movieCopies.add(new MovieCopy(qResults.getInt(1), MovieCopy.Medium.valueOf(qResults.getString(2)), MovieCopy.MovieFormat.valueOf(qResults.getString(3)),
                    qResults.getFloat(4)));
        }

        return movieCopies.toArray(new MovieCopy[0]);
    }

    /**
     * Todo: remove, for testing purposes.
     * @param args
     */
    public static void main(String[] args) {
        Actor a = new Actor(null, "Sterling Hayden");

        try {
            //Data dataSource = new Data("/home/john/Documents/Μαθήματα/5ο Εξάμηνο/Μηχανική Λογισμικού/Εργασία Εξαμήνου/GraphQL_Project/database/video_club.sqlite");
            Data d = new Data("database/video_club.sqlite");

            Actor[] aa = d.retrieveActors(a);

            for (Actor ac : aa){
                System.out.println(ac);
            }

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }



}
