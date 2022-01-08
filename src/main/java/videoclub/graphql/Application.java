package videoclub.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import videoclub.datastore.Data;

import java.sql.SQLException;


@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

    Data d;

    {
        try {
            Data d = new Data("");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}