package videoclub.graphql.server.resolvers.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.*;

import java.sql.DriverManager;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.OffsetDateTime;

/**
 * Resolver for the queries supported by the server.
 * @author Ioannis Baraklilis
 */
@Component
public class QueryResolver implements GraphQLQueryResolver {
    /**
     * Resolves the GraphQL query:
     * getCustomer(id: ID, name: String): [Customer!].
     */
    public Customer[] getCustomer(Integer id, String fullName) throws SQLException {
        Customer template = new Customer(id, fullName, null, null, null, null);
        return Application.dataSource.aboutCustomers().retrieveCustomers(template);
    }

    /**
     * Resolves the GraphQL query:
     * getCategory(name: String): [Category!].
     */
    public Category[] getCategory(Integer id, String name) throws SQLException {
        Category template = new Category(id, name);
        return Application.dataSource.aboutCategories().retrieveCategories(template);
    }

    /**
     * Resolves the GraphQL query:
     * getActor(id: ID, name: String): [Actor!].
     */
    public Actor[] getActor(Integer id, String name) throws SQLException {
        Actor template = new Actor(id, name);
        return Application.dataSource.aboutActors().retrieveActors(template);
    }

    /**
     * Resolves the GraphQL query:
     * getDirector(id: ID, name: String): [Director!].
     */
    public Director[] getDirector(Integer id, String name) throws SQLException {
        Director template = new Director(id, name);
        return Application.dataSource.aboutDirectors().retrieveDirectors(template);
    }

    /**
     * Resolves the GraphQL query:
     * getMovieCopy(id: ID, medium: Medium, format: MovieFormat): [MovieCopy!].
     */
    public MovieCopy[] getMovieCopy(Integer id, MovieCopy.Medium medium, MovieCopy.MovieFormat format) throws SQLException {
        MovieCopy template =  new MovieCopy(id, medium, format, null);
        return Application.dataSource.aboutMovieCopies().retrieveMovieCopy(template);
    }

    /**
     * Resolves the GraphQL query:
     * getMoviesByPrice(from:Float, upTo: Float): [MovieTitle!].
     */
    public MovieCopy[] getMovieCopiesByPrice(Float from, Float upTo) throws SQLException {
        return Application.dataSource.aboutMovieCopies().retrieveMovieCopiesInPriceRange(from, upTo);
    }

    /**
     * Resolves the GraphQL query:
     * getMovies(id: ID, title: String, releaseDate: Date, ratingFrom: Float, ratingTo: Float): [MovieTitle!].
     */
    public MovieTitle[] getMovies(Integer id, String title, LocalDate releaseDate, Float ratingFrom, Float ratingTo) throws SQLException {
        if (id != null){ // If the id argument is used, the others will be ignored.
            return Application.dataSource.aboutMovieTitles().retrieveMovieTitles(
                    new MovieTitle(id, null, null, null, null)
            );
        } else {
            // Retrieve all the movie copies stored a
            return Application.dataSource.aboutMovieTitles().retrieveMovieTitlesWithFiltering(title, releaseDate, ratingFrom, ratingTo);
        }
    }

    /**
     * Resolves the GraphQL query:
     * getProductionCompany(id: ID, name: String): [ProductionCompany!].
     */
    public ProductionCompany[] getProductionCompany(Integer id, String name) throws SQLException {
        ProductionCompany template = new ProductionCompany(id, name);
        return Application.dataSource.aboutProductionCompanies().retrieveProductionCompanies(template);
    }

    /**
     * Resolves the GraphQL query:
     * getRents(id: ID, priceFrom: Float, priceTo: Float, dateFrom: DateTime, dateFromUpto: DateTime): [RentTransaction!].
     */
    public RentTransaction[] getRents(Integer id, Float priceFrom, Float priceTo, OffsetDateTime dateFrom, OffsetDateTime dateFromUpto) throws SQLException {
        if ( id!= null){ // If the id argument is used, the others will be ignored.
            return Application.dataSource.aboutRentTransactions().retrieveRentTransactions(new RentTransaction(id, null, null, null));
        } else {
            return Application.dataSource.aboutRentTransactions().retrieveRentTransactionsFiltered(priceFrom, priceTo, dateFrom, dateFromUpto);
        }
    }
}
