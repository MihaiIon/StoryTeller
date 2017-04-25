package app.storyteller.manager;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.v4.content.ContextCompat;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

import app.storyteller.R;
import app.storyteller.api.GoogleApiHelper;
import app.storyteller.models.Account;

/**
 * Created by Mihai on 2017-03-12.
 *
 * This class is used to keep track of the current logged User and
 * activities changes.
 */

public class AppManager {

    /**
     *
     */
    private static AccountManager accountManager;


    //-------------------------------------------------------
    // Methods

    public static void init(){
        setAccountManager(null);
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
    public static boolean isSignedIn(Context ctx){
        return getGoogleApiClient(ctx).isConnected();
    }


    //-------------------------------------------------------
    // Getters and Setters

    public static Account getAccount(){ return accountManager.getAccount(); }
    public static AccountManager getAccountManager(){ return accountManager; }
    public static TokenManager getTokenManager(){ return accountManager.getTokenManager(); }

    /**
     *
     * @return
     */
    public static GoogleApiClient getGoogleApiClient(Context ctx){
        return new GoogleApiHelper(ctx).getGoogleApiClient();
    }

    /**
     *
     * @param account : .
     */
    public static void setAccountManager(Account account){
        accountManager = new AccountManager(account);
    }



}
