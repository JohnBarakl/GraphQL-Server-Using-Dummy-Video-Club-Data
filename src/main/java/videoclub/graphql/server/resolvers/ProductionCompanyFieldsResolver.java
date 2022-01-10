package videoclub.graphql.server.resolvers;

import graphql.kickstart.tools.GraphQLResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.Application;
import videoclub.graphql.server.domain.videoclub.MovieTitle;
import videoclub.graphql.server.domain.videoclub.ProductionCompany;

import java.sql.SQLException;

/**
 * This class contains methods to resolve field queries of the type ProductionCompany that is not
 * explicitly stored as an instance variable of an object of this kind.
 */
@Component
public class ProductionCompanyFieldsResolver implements GraphQLResolver<ProductionCompany> {
    /**
     * Acts as resolver for the moviesProduced field of ProductionCompany type.
     * @param productionCompany The {@link ProductionCompany} that produced the movies requested.
     * @return The list of movie titles requested.
     * @throws SQLException If there is a communication error with the data source.
     */
    public MovieTitle[] moviesProduced(ProductionCompany productionCompany) throws SQLException {
        return Application.dataSource.retrieveMovieTitlesOfProductionCompany(productionCompany);
    }
}
