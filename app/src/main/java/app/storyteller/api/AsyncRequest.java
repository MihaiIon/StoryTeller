package app.storyteller.api;

import android.content.Context;
import android.os.AsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.database.DBHandler;
import app.storyteller.models.Account;
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
     *
     */
    private Context context;

    /**
     * Constructor.
     *
     * @param request : Your request for Api.
     */
    public AsyncRequest(Request request, Context context){
        this.request = request;
        this.context = context;
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

        //
        String response = "";

        try
        {
            // Build url.
            URL url = new URL(request.getUrl());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + request.getUrl());
            // Set Connection.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json");
            conn.setRequestProperty("User-Agent", "");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            // Add JSON.
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
            wr.writeBytes(request.getJSON().toString());
            wr.flush();
            wr.close();

            // Connect to API.
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
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>>" + response);
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        switch (request.getAction())
        {
            case Request.Actions.CREATE_PROFILE:
                System.out.println(
                    "************************************"
                    +"\nProfile Created on API.\n"
                    +"************************************"
                );
                try{
                    JSONObject obj = new JSONObject(response);
                    Account acc = new Account(
                        obj.getInt("id"),
                        obj.getString("google_id"),
                        obj.getString("name"),
                        obj.getInt("tokens"),
                        obj.getString("image_url"),
                        Timestamp.valueOf(obj.getString("last_connected")),
                        new ArrayList<Story>()
                    );
                    System.out.println(acc);

                    // -- Add Profile to local DB.
                    DBHandler.openConnection(context);
                    DBHandler.createAccount(acc);
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
