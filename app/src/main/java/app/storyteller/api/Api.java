package app.storyteller.api;


import android.content.Context;
import android.support.v7.app.AppCompatActivity;

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
    public static void executeRequest(Request request, AppCompatActivity activity)
    {
        new AsyncRequest(request, activity);
    }
}
