package app.storyteller.api;

import android.content.Context;
import android.net.ConnectivityManager;
import android.os.AsyncTask;
import android.support.v4.app.FragmentActivity;
import android.provider.Settings.Secure;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.Date;

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

    /**
     * Provides an unique ID for the current Android device. This ID is used
     * by the API to differentiate it between from all other devices.
     */
    private static String deviceID;

    /**
     * Provides access to information about the device connectivity.
     */
    private static ConnectivityManager cm;



    //------------------------------------------------------------------


    /**
     * Wakes up Api.
     *
     * @return
     */
    public static boolean init(FragmentActivity app){

        // Keep a local copy of the ConnectivityManager.
        cm = (ConnectivityManager)
                app.getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);

        // Get the current device ID.
        deviceID = Secure.getString(
                app.getApplicationContext().getContentResolver(),
                Secure.ANDROID_ID );

        return true;
    }

    /**
     *  Checks if the current Android Device is connected to the internet.
     */
    public static boolean isConnected(){
        return cm.getActiveNetworkInfo().isConnected();
    }

    /**
     * Connects to the API and executes a request.
     *
     * @return
     */
    private static void executeRequest(Request request){ new Sender(request); }

    /**
     * Returns a well formatted URL to access the API.
     *
     * @param request :
     */
    private static String buildUrlFromRequest(Request request){
        return API_URL + request.getAction() + request.getParams();
    }




    //------------------------------------------------------------------




    /**
     * Api needs a Race from your device, to the API, and back again to your device
     * in order to access the API and retrieve data from it (if needed).
     */
    private static class Sender extends AsyncTask<Object, Integer, String>{

        /*
         * Attribute.
         */
        private Request request;

        /**
         * Constructor.
         *
         * @param request : Your request for Api.
         */
        public Sender(Request request){
            this.request = request;
            execute();
        }

        /**
         * Async Call to the API.
         *
         * @param params : params[0] -> request.
         * @return : An empty string if there is no need for a response from the API
         *           - or - The data (JSON format) received from the API.
         */
        @Override
        protected String doInBackground(Object[] params) {

            // Response from API.
            String response = "";

            try
            {
                // Build url.
                URL url = new URL(buildUrlFromRequest(request));

                // Connect to the API.
                HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.connect();

                // If the request needs a response from the API, read the response...
                if (request.needsResponse())
                {
                    InputStream is = conn.getInputStream();
                    BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String data;
                    while ((data = reader.readLine()) != null){
                        response += data;
                    }
                }
            }
            catch(Exception e){
                System.out.println(e);
            }

            /*
             *
             */
            return response;
        }

        @Override
        protected void onPostExecute(String response) {
            super.onPostExecute(response);
            String action = request.getAction();
            switch (action.substring(0,action.length()-1))
            {
                case Request.Actions.CREATE_PROFILE:
                    System.out.println(
                        "************************************"
                        +"\nProfile Created on API.\n"
                        +"************************************"
                    );
                    break;
                case Request.Actions.RESET_DATABASE:
                    System.out.println(
                        "************************************"
                        +"\nDBHandler has been Reset on API.\n"
                        +"************************************"
                    );
                    break;
            }
        }
    }




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
     * @param:  The name of the profile.
     * //@return  JSON Object of the created Profile.
     */
    public static void createProfile(String name){

        // Get date format.
        Timestamp timestamp = new Timestamp(new Date().getTime());

        // Build params.
        String[] params = new String[]{
            API_KEY, name, deviceID,
            ""+timestamp.getTime()  // Current date.
        };

        // Create and execute request.
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
