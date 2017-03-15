package app.storyteller.manager;

import android.app.Application;
import android.content.Context;
import android.net.ConnectivityManager;

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
     * Current context in the application.
     * -- Useful for an easy access to the database.
     */
    private static Context context;

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
        StoryTellerManager.context = getApplicationContext();
    }



    //-------------------------------------------------------
    // Getters and Setters

    public static Profile getProfile(){ return profile; }
    public static Context getContext(){ return context; }

    public static void setProfile(Profile p){ profile = p; }
    public static void setContext(Context c){ context = c; }


    //-------------------------------------------------------
    // Methods


    /**
     * Initializes the StoryTellerManager.
     * @param google_id : TODO.
     */
    public static boolean init(Context ctx, String google_id) {
        if (context == null)
            context = ctx;

        return true;
    }

    /**
     *  Checks if the current Android Device is connected to the internet.
     */
    public static boolean isConnected(){
        return ((ConnectivityManager)
                context.getSystemService(Context.CONNECTIVITY_SERVICE))
                        .getActiveNetworkInfo().isConnected();
    }
}
