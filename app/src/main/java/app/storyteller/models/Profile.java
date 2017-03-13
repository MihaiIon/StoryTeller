package app.storyteller.models;

import java.sql.Timestamp;
import java.util.ArrayList;


public class Profile extends User {

    /*
     * Attributes
     */
    private int tokens;
    private ArrayList<Story> favorites;

    /**
     * Constructors
     * -- A Profile is what is created locally to represent the current User.
     *    Additional features are : Tokens and Favorites.
     */
    public Profile(int id, int google_id, String name, int tokens, String imageURL, Timestamp lastConnected, ArrayList<Story> favorites) {
        super(id, google_id, name, imageURL, lastConnected);
        this.tokens = tokens;
        this.favorites = favorites;
    }

    public Profile(User user, int tokens, ArrayList<Story> favorites) {
        super(user.getId(), user.getGoogleId(), user.getName(),
                user.getImageURL(), user.getLastConnected());
        this.tokens = tokens;
        this.favorites = favorites;
    }


    //------------------------------------------------------------
    // Getters and Setters

    public int getId() { return super.getId(); }
    public int getGoogleId() { return super.getGoogleId(); }
    public String getName() { return super.getName(); }
    public int getTokens() { return tokens; }
    public String getImageURL() { return super.getImageURL(); }
    public ArrayList getFavorites() { return favorites; }
    public Timestamp getLastConnected() { return super.getLastConnected(); }

    public void setName(String name) {
        super.setName(name);
    }
    public void setTokens(int tokens) {
        this.tokens = tokens;
    }
    public void setImagePath(String imageURL) { super.setImagePath(imageURL); }


    //------------------------------------------------------------
    // Methods

    /**
     * TODO. -> Returns a list of all the stories which this profile participated.
     * @return :
     */
    public ArrayList<Story> hasParticipatedIn() {
        return new ArrayList<Story>();
    }

    /**
     * Checks if the current User (Profile) is connect to the Internet.
     * Being disconnect for internet will produce the following probles: The user
     * will not be able to create, add a sentence to a story, check new stories, etc.
     *
     * TODO.
     */
    @Override
    public boolean isConnected() {
        return super.isConnected();
    }

    /**
     * Trivial.
     */
    public Profile updateLastConnected(){
        super.setLastConnected(new Timestamp(System.currentTimeMillis()));
        return this;
    }

    /**
     *
     * @param story
     */
    public void addFavorite(Story story) {
        int i = this.favorites.indexOf(story); // Return 0 if the story is not in the list.
        if(i < 0) {
            this.favorites.add(story);
        }
        else throw new IllegalArgumentException("Story already present");
    }

    /**
     *
     * @param story
     */
    public void removeFavorite(Story story) {
        int i = this.favorites.indexOf(story);
        if(i >= 0) {
            this.favorites.remove(i);
        }
        else throw new IllegalArgumentException("Story is not present");
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + getId() +
                ", google_id=" + getGoogleId() +
                ", name='" + getName() + '\'' +
                ", imageURL='" + getImageURL() + '\'' +
                ", tokens=" + tokens +
                ", favorites=" + favorites +
                ", lastConnected='" + getLastConnected() + '\'' +
                '}';
    }
}
