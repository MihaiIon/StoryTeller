package app.storyteller.models;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.database.DBHandler;


public class Profile extends User {

    /*
     * Attributes
     */
    private int tokens;
    private ArrayList<Story> favorites;

    /**
     * Constructors
     */
    public Profile(int id, String google_id, String name, int tokens, String imageURL, Timestamp lastConnected, ArrayList<Story> favorites) {
        super(id, google_id, name, imageURL, lastConnected);
        this.tokens = tokens;
        this.favorites = favorites;
    }

    //------------------------------------------------------------
    // Getters and Setters

    public int getId() { return super.getId(); }
    public String getGoogleId() { return super.getGoogleId(); }
    public String getName() { return super.getName(); }
    public int getTokens() { return tokens; }
    public String getImageURL() { return super.getImageURL(); }
    public ArrayList getFavorites() { return favorites; }
    public Timestamp getLastConnected() { return super.getLastConnected(); }

    public void setName(String name) {
        super.setName(name);
    }
    public void setImagePath(String imageURL) { super.setImagePath(imageURL); }

    //------------------------------------------------------------
    // Methods

    /**
     * Tokens Methods.
     */
    public void grantToken()  { if (this.tokens < 3) this.tokens++; }
    public void consumeToken(){ if (this.tokens > 1) this.tokens--; }


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
