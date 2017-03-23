package app.storyteller.manager;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

import com.google.android.gms.common.api.GoogleApiClient;

import app.storyteller.models.Profile;

/**
 * Created by Mihai on 2017-03-12.
 *
 * This class is used to keep track of the current logged User and
 * activities changes.
 *
 * It is a useful class for the saving data to the clouds and on the
 * local database.
 */

public class StoryTellerManager extends Application{

    /**
     * Provides access to the Google API to retrieve information about accounts
     */
    private static GoogleApiClient gac;

    /**
     * Current logged-in Profile or the last Profile that logged-in
     * since the last visit.
     */
    private static Profile profile;

    /**
     *
     */
    public void onCreate() {
        super.onCreate();
    }


    //-------------------------------------------------------
    // Getters and Setters

    public static Profile getProfile(){ return profile; }
    public static GoogleApiClient getGoogleApiClient(){ return gac; }

    public static void setProfile(Profile p){ profile = p; }
    public static void setGoogleApiClient(GoogleApiClient g){ gac = g; }


    //-------------------------------------------------------
    // Methods


    /**
     *
     */
    public static boolean init(GoogleApiClient g){
        setGoogleApiClient(g);
        return true;
    }

    /**
     *  Checks if the current Android Device is connected to the internet.
     */
    public static boolean isConnected(){ return true; }

    /**
     *  Checks if the current User has signed in with a Google Account.
     */
    public static boolean isSignedIn(){
        return gac.isConnected();
    }
}
