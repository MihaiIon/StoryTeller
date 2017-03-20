package app.storyteller.api;

import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.database.DBHandler;
import app.storyteller.models.Profile;
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-03-15.
 *
 * Launches a thread to save or retrieve data from the Api.
 */
public class AsyncRequest extends AsyncTask<Object, Integer, String> {

    /**
     *
     */
    private Request request;

    /**
     * Constructor.
     *
     * @param request : Your request for Api.
     */
    public AsyncRequest(Request request){
        this.request = request;
        execute();
    }

    /**
     * Returns a well formatted URL to access the API.
     *
     * @param request :
     */
    private String buildUrlFromRequest(Request request){
        return Api.getApiUrl() + request.getAction() + request.getParams();
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
        catch(Exception e){ e.printStackTrace(); }

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
                    + "\n" + response
                );
                try{
                    JSONObject obj = new JSONObject(response);
                    Profile p = new Profile(
                        obj.getInt("id"),
                        obj.getString("google_id"),
                        obj.getString("name"),
                        obj.getInt("tokens"),
                        obj.getString("image_url"),
                        Timestamp.valueOf(obj.getString("last_connected")),
                        new ArrayList<Story>()
                    );
                    System.out.println(p);

                    // -- Add Profile to local DB.
                    DBHandler.openConnection();
                    DBHandler.createAccount(p);
                    DBHandler.closeConnection();

                } catch(JSONException e){ e.printStackTrace(); }
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
