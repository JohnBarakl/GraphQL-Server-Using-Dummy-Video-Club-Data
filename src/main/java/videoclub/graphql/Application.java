package videoclub.graphql;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import videoclub.datastore.Data;

import java.sql.SQLException;



@SpringBootApplication
public class Application {
    public static Data dataSource;

    public static void main(String[] args) {
        try {
            dataSource = new Data("database/video_club.sqlite");
        } catch (SQLException e) {
            System.err.println("Database Error:");
            e.printStackTrace();
        }

        try {
            SpringApplication.run(Application.class, args);
        } catch (Exception e){
            System.err.println("=========================  Exception Caught  =========================");
            e.printStackTrace();
            System.err.println("=========================  End Of Exception  =========================");
        }
    }

}