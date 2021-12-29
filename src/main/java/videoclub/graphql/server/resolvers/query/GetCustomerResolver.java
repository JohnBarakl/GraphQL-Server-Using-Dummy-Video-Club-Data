package videoclub.graphql.server.resolvers.query;

import graphql.kickstart.tools.GraphQLQueryResolver;
import org.springframework.stereotype.Component;
import videoclub.graphql.server.domain.videoclub.Customer;

import java.time.LocalDate;
import java.util.ArrayList;

/**
 * Resolver for the query getCustomerByID.
 * It returns the Customer object(s) (from the database) that match the arguments.
 * If no arguments are provided it returns all customers.
 * @author Ioannis Baraklilis
 */
@Component
public class GetCustomerResolver implements GraphQLQueryResolver {
    public Customer[] getCustomer(Integer id, String fullName) {
        // TODO: Utilise database.
        ArrayList<Customer> temp = new ArrayList<>();
        String name;
        name = "John";
        temp.add(new Customer(0, name, LocalDate.parse("2019-10-19"), "Addr_" + name, name + "_phone", name + "_mail"));
        name = "Jim";
        temp.add(new Customer(0, name, LocalDate.parse("2020-10-19"), "Addr_" + name, name + "_phone", name + "_mail"));
        name = "Jane";
        temp.add(new Customer(0, name, LocalDate.parse("2018-10-19"), "Addr_" + name, name + "_phone", name + "_mail"));
        name = "Jimbo";
        temp.add(new Customer(0, name, LocalDate.parse("2017-10-19"), "Addr_" + name, name + "_phone", name + "_mail"));

        if (id != null) {
            int i = 0;
            while (i < temp.size()) {
                if (!temp.get(i).getId().equals(id)) {
                    temp.remove(i);
                    continue;
                }
                i++;
            }
        }

        if (fullName != null) {
            int i = 0;
            while (i < temp.size()) {
                if (!temp.get(i).getFullName().equals(fullName)) {
                    temp.remove(i);
                    continue;
                }
                i++;
            }
        }

        return (Customer[]) temp.toArray(new Customer[1]);
    }
}
