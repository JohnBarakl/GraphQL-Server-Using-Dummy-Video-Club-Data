package videoclub.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import videoclub.datastore.DataIntersection;

import java.sql.SQLException;



@SpringBootApplication
public class Application {
    public static DataIntersection dataSource;

    public static void main(String[] args) {
        try {
            dataSource = new DataIntersection("database/video_club.sqlite");
        } catch (SQLException e) {
            System.err.println("Database Error:");
            e.printStackTrace();
        }

        SpringApplication.run(Application.class, args);
    }

}