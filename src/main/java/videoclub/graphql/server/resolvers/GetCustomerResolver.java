package videoclub.graphql.server.resolvers;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.server.domain.videoclub.Customer;

import java.util.UUID;

@Component
public class GetCustomerResolver implements GraphQLQueryResolver {
    public Customer getCustomer(UUID id){
        return new Customer(id);
    }
}
