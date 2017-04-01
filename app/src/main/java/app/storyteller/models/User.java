package app.storyteller.models;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Mihai on 2017-03-10.
 */
public class User {

    /*
     * Attributes
     */
    private int id;         // Index in the database.
    private String name;
    private String google_id;
    private String imageURL;
    private Timestamp lastConnected;

    /**
     * Default Constructor.
     * -- A User is a person that interacts with the app.
     */
    public User(int id, String google_id, String name, String imageURL, Timestamp lastConnected){
        this.id = id;
        this.google_id = google_id;
        this.name = name;
        this.imageURL = imageURL;
        this.lastConnected = lastConnected;
    }

    /**
     * Quick Constructor.
     * -- Used for editing a Story (See AsyncTask).
     */
    public User(int id){
        this.id = id;
        this.google_id = null;
        this.name = null;
        this.imageURL = null;
        this.lastConnected = null;
    }

    //------------------------------------------------------------
    // Getters and Setters

    public int getId() { return id; }
    public String getGoogleId() {
        return google_id;
    }
    public String getName() {
        return name;
    }
    public String getImageURL() {
        return imageURL;
    }
    public Timestamp getLastConnected() { return lastConnected; }

    public void setName(String name) {
        this.name = name;
    }
    public void setImagePath(String imageURL) {
        this.imageURL = imageURL;
    }
    public void setLastConnected(Timestamp t) { this.lastConnected = t; }


    //------------------------------------------------------------
    // Methods

    /**
     * Checks if the user is online. TODO.
     */
    public boolean isConnected(){
        return true;
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", google_id=" + google_id +
                ", imageURL='" + imageURL + '\'' +
                ", lastConnected=" + lastConnected +
                '}';
    }
}
