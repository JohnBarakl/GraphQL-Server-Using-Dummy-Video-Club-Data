package videoclub.graphql.server.domain.videoclub.input;

/**
 * The object equivalent to the input type of the same name used to accept input for the purposes
 * of deleting an existing movie copy.
 */
public class DeleteMovieCopyInput {

    /** The id of the movie copy that should be deleted. */
    Integer movieCopyID;

    public Integer getMovieCopyID() {
        return movieCopyID;
    }
}
