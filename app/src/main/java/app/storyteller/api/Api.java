package app.storyteller.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.provider.Settings.Secure;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import app.storyteller.database.DBHandler;
import app.storyteller.manager.StoryTellerManager;
import app.storyteller.models.Profile;
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-01-30.
 */
public class Api {

    /**
     * The Url to connect to the api.
     */
    private static String API_URL = "http://storytellerapp.stephanpelletier.com/api.php/";

    /**
     * The following KEY is essential to connect to the API. If the KEY
     * is not provided, the connection fails.
     */
    private static String API_KEY = "a518708fe7632448ceaf";



    //------------------------------------------------------------------


    /**
     * Connects to the API and executes a request.
     *
     * @return
     */
    private static void executeRequest(Request request){ new AsyncRequest(request); }


    /**
     * Provides the API's url.
     */
    public static String getApiUrl() { return API_URL; }


    //------------------------------------------------------------------



    /********************************************
    *											*
    *											*
    *	Profile Related							*
    *											*
    *											*
    ********************************************/

    /**
     * Requests the API to create a new Profile in the database. The API then
     * returns a JSON object of that new created Profile so that it can be
     * saved locally.
     *
     * @param google_id :  The Google ID associated with this Profile.
     * @param name      :  The Profile's name.
     * @param imageURL  :  The Profile's image URL.
     */
    public static void createProfile(String google_id, String name, String imageURL){
        String[] params = new String[]{ API_KEY, google_id, name, imageURL };
        executeRequest(new Request(Request.Actions.CREATE_PROFILE, params, true));
    }

    /**
     * TODO.
     */
    public static void updateProfile(){

    }


    /********************************************
    *											*
    *											*
    *	Story Related							*
    *											*
    *											*
    ********************************************/





    /********************************************
    *											*
    *											*
    *	For Debug Purpose						*
    *											*
    *											*
    ********************************************/

    /**
     *	FOR DEBUG -- Resets the content of all tables (and resets the IDs).
     */
    public static void resetDatabase(){
        // Create and execute request.
        executeRequest(new Request(Request.Actions.RESET_DATABASE, new String[]{API_KEY}, false));
    }
}
