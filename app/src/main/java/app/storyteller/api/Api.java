package app.storyteller.api;


import android.app.Activity;
import android.support.v4.app.Fragment;

/**
 * Created by Mihai on 2017-01-30.
 */
public class Api {


    /**
     * The Url to connect to the api.
     */
    protected static String API_URL = "http://storytellerapp.stephanpelletier.com/api.php/";

    /**
     * The following KEY is essential to connect to the API. If the KEY
     * is not provided, the connection fails.
     */
    protected static String API_KEY = "a518708fe7632448ceaf";


    //------------------------------------------------------------------


    /**
     * Connects to the API and executes a request.
     *
     * @return
     */
    public static void executeRequest(Request request, Activity activity)
    {
        new AsyncRequest(request, activity);
    }

    public static void executeRequest(Request request, Fragment fragment)
    {
        new AsyncRequest(request, fragment);
    }
}
