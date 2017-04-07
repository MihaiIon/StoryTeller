package app.storyteller.manager;

import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import com.google.android.gms.common.api.GoogleApiClient;

import app.storyteller.models.Account;

/**
 * Created by Mihai on 2017-03-12.
 *
 * This class is used to keep track of the current logged User and
 * activities changes.
 */

public class StoryTellerManager{

    /**
     * Provides access to the Google API to retrieve information about accounts
     */
    private static GoogleApiClient gac;

    /**
     * Current logged-in Profile or the last Profile that logged-in
     * since the last visit.
     */
    private static Account account;

    /**
     *
     * @return
     */
    private static Bitmap accountImage;


    //-------------------------------------------------------
    // Getters and Setters

    public static Account getAccount(){ return account; }
    public static GoogleApiClient getGoogleApiClient(){ return gac; }
    public static Bitmap getAccountImage(){ return accountImage; }

    public static void setAccount(Account a){ account = a; }
    public static void setGoogleApiClient(GoogleApiClient g){ gac = g; }
    public static void setAccountImage(Bitmap bitmap){ accountImage = bitmap; }


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
    public static boolean isConnectedToInternet(Context c) {
        ConnectivityManager connectivityManager = (ConnectivityManager) c.getSystemService(Context.CONNECTIVITY_SERVICE);
        return (connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_MOBILE).getState() == NetworkInfo.State.CONNECTED ||
                connectivityManager.getNetworkInfo(ConnectivityManager.TYPE_WIFI).getState() == NetworkInfo.State.CONNECTED);
    }

    /**
     *  Checks if the current User has signed in with a Google Account.
     */
    public static boolean isSignedIn(){
        return gac.isConnected();
    }

    /**
     *
     */
    public static boolean isAccountCreated() { return account != null; }
}
