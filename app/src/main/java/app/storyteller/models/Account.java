package app.storyteller.models;

import java.sql.Timestamp;
import java.util.ArrayList;

/**
 * Created by Mihai on 2017-03-25.
 */

public class Account extends Profile {

    /*
     * Attributes.
     */
    private boolean signedIn;


    /**
     * Constructor.
     * -- Google/Game Account on this device.
     */
    public Account(int id, String google_id, String name, int tokens, String imageURL, Timestamp lastConnected, ArrayList<Story> favorites){
        super(id, google_id, name, tokens, imageURL, lastConnected, favorites);
        signIn();
    }



    //--------------------------------------------------------------------
    // Methods.

    /**
     * TODO.
     */
    public void signIn() {
        this.signedIn = true;
        connect();
    }

    /**
     * TODO.
     */
    public void signOut() {
        this.signedIn = false;
        disconnect();
    }

    /**
     * TODO. -> Connect to API.
     */
    public void connect() {}

    /**
     * TODO. -> Disconnect from API.
     */
    public void disconnect() {}


    //--------------------------------------------------------------------


    @Override
    public String toString() {
        return "Account{" +
                "id=" + getId() +
                ", google_id=" + getGoogleId() +
                ", name='" + getName() + '\'' +
                ", imageURL='" + getImageURL() + '\'' +
                ", tokens=" + getTokens() +
                ", favorites=" + getFavorites() +
                ", lastConnected='" + getLastConnected() + '\'' +
                ", signedIn='" + signedIn + "\'" +
                '}';
    }
}
