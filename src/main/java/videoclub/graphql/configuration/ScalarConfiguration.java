package videoclub.graphql.configuration;

import graphql.scalars.ExtendedScalars;
import graphql.schema.GraphQLScalarType;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Configuration needed for scalar types defined by the extended scalars dependency to work.
 */
@Configuration
public class ScalarConfiguration {

    /**
     * Configuration for the Date scalar.
     * @return An instance of the DateScalar type.
     */
    @Bean
    public GraphQLScalarType date(){
        return ExtendedScalars.Date;
    }

    /**
     * Configuration for the DateTime scalar.
     * @return An instance of the DateTimeScalar type.
     */
    @Bean
    public GraphQLScalarType dateTime(){
        return ExtendedScalars.DateTime;
    }

}
