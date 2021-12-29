package videoclub.datastore;

import videoclub.graphql.server.domain.videoclub.Actor;
import videoclub.graphql.server.domain.videoclub.Category;

import java.util.ArrayList;

/**
 * The class that manages the application's data.
 * It uses an SQLite database and the JDBC api to load, process and save data.
 * @author Ioannis Baraklilis
 */
public class Data {
    private ArrayList<Actor> actors;

    public Data() {

    }

    /**
     * This class provides methods for I/O with the database regarding the Actor entities.
     */
    public class ActorIO {
        /**
         * Creates and inserts a new actor in the database.
         * @param actor The actor that will be saved in the database
         * @return The success of this attempt.
         *         <p>An empty string represents success. In any other case, the string will contain an appropriate error message.</p>
         */
        public String createNew(Actor actor){

        }

        /**
         * Retrieves an actor from the database.
         * @param actor The actor template that will be used for selection of the actors loaded from the database.
         *              <p>Any object fields that have the value null will be substituted for any value</p>
         * @return A list of Actor entities that match the template given as argument.
         */
        public Actor[] retrieve(Actor actor){

        }

        /**
         *
         * @param oldActor
         * @param newActor
         * @return
         */
        public boolean mutate(Actor oldActor, Actor newActor){

        }

        /**
         *
         * @param actor
         * @return
         */
        public boolean delete(Actor actor){

        }
    }

    /**
     * This class provides methods for I/O with the database regarding the Actor entities.
     */
    public class CategoryIO {
        /**
         * Creates and inserts a new category in the database.
         * @param category The category that will be saved in the database
         * @return The success of this attempt.
         *         <p>An empty string represents success. In any other case, the string will contain an appropriate error message.</p>
         */
        public String createNew(Category category){

        }

        /**
         * Retrieves a category from the database.
         * @param category The category template that will be used for selection of the actors loaded from the database.
         *              <p>Any object fields that have the value null will be substituted for any value</p>
         * @return A list of Category entities that match the template given as argument.
         */
        public Actor[] retrieve(Category category){

        }

        /**
         *
         * @param oldCategory
         * @param newCategory
         * @return
         */
        public boolean mutate(Category oldCategory, Category newCategory){

        }

        /**
         *
         * @param category
         * @return
         */
        public boolean delete(Category category){

        }
    }

}
