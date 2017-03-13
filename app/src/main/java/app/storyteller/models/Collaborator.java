package app.storyteller.models;

/**
 * Created by Mihai on 2017-03-10.
 */


public class Collaborator {

    /*
     * Attributes.
     */
    private int id;             // Index in the database.
    private User user;
    private Story story;

    /**
     * Constructor.
     * @param id    : Index of the collaborator in the database.
     * @param user  : The User associated with this Collaborator.
     * @param s     : The Story associated with this collaborator.
     */
    public Collaborator(int id, User user, Story s){
        this.id = id;
        this.user = user;
        this.story = s;
    }

    public int getId() { return id; }
    public User getUser() { return user; }
    public Story getStory() { return story; }
}
