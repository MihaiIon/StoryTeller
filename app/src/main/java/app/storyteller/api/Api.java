package app.storyteller.api;


import android.content.Context;

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
    public static void executeRequest(Request request, Context context)
    {
        new AsyncRequest(request, context);
    }


    //------------------------------------------------------------------



    /**
     * TODO.
     */
    private static void updateProfile(){

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
    private static void resetDatabase(){
        // Create and execute request.
        //executeRequest(new Request(Request.Actions.RESET_DATABASE, new String[]{API_KEY}, false));
    }
}
