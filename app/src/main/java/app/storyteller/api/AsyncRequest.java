package app.storyteller.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;

import org.json.JSONArray;
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

import app.storyteller.StoryChooserActivity;
import app.storyteller.StoryCreatorActivity;
import app.storyteller.database.DBHandler;
import app.storyteller.manager.StoryTellerManager;
import app.storyteller.models.Account;
import app.storyteller.models.Sentence;
import app.storyteller.models.Story;
import app.storyteller.models.StoryDetails;
import app.storyteller.models.User;

/**
 * Created by Mihai on 2017-03-15.
 *
 * Launches a thread to save or retrieve data from the Api.
 */
public class AsyncRequest extends AsyncTask<Object, Integer, String> {

    /**
     * Current activity that ordered a AsyncRequest.
     */
    private AppCompatActivity activity;

    /**
     * The resquest sent to the API.
     */
    private Request request;

    /**
     * Constructors
     */
    public AsyncRequest(Request request, AppCompatActivity activity){
        this.request = request;
        this.activity = activity;
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
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> URL : " + request.getUrl());
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> REQUEST : " + request.getJSON().toString());

            // Set Connection.
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestProperty("Content-Type","application/json; charset=UTF-8");
            conn.setRequestMethod("POST");
            conn.setDoInput(true);

            // Add JSON.
            DataOutputStream wr = new DataOutputStream(conn.getOutputStream ());
            wr.write(request.getJSON().toString().getBytes("UTF-8"));
            wr.flush();
            wr.close();

            // Connect to API.
            conn.connect();
            System.out.println(">>>>>>>>>>>>>>>>>>>>>>> RESPONSE CODE : " + conn.getResponseCode());

            // If the request needs a response from the API, read the response...
            InputStream is = conn.getInputStream();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            String data;
            while ((data = reader.readLine()) != null){
                response += data;
            }
        }
        catch(Exception e){ e.printStackTrace(); }
        System.out.println(">>>>>>>>>>>>>>>>>>>>>>> RESPONSE : " + response);
        return response;
    }

    @Override
    protected void onPostExecute(String response) {
        super.onPostExecute(response);

        switch (request.getAction())
        {
            //----------------------------------------------------------------------
            /*
             * Profile Related.
             */
            case ApiRequests.Actions.CREATE_PROFILE:
            case ApiRequests.Actions.UPDATE_PROFILE:
                System.out.println(
                    "************************************"
                    +"\nProfile Created/Updated on API.\n"
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

                    // -- Add/Update Account in local DB.
                    DBHandler.openConnection(activity.getApplicationContext());
                    if (request.getAction().equals(ApiRequests.Actions.CREATE_PROFILE))
                         DBHandler.createAccount(acc);
                    else DBHandler.updateAccount(acc);
                    StoryTellerManager.setAccount(acc);
                    DBHandler.closeConnection();

                    // -- Proceed to MainActivity.
                    activity.finish();
                } catch(JSONException e){ e.printStackTrace(); }
                break;

            //----------------------------------------------------------------------
            /*
             * Story related.
             */
            case ApiRequests.Actions.CREATE_STORY:
            case ApiRequests.Actions.UPDATE_STORY:
                // -- Destroy StoryEditorActivity.
                activity.finish();
                break;
            case ApiRequests.Actions.LOCK_STORY:
            case ApiRequests.Actions.UNLOCK_STORY:
                break;
            case ApiRequests.Actions.IS_STORY_LOCKED:
                try{
                    JSONObject obj = new JSONObject(response);
                    if (obj.getInt("value") == 1){ // 1: true; 2: false

                    } else {

                    }

                }catch(JSONException e){ e.printStackTrace(); }
                break;


            //----------------------------------------------------------------------
            /*
             * Story related - Fetches
             */
            case ApiRequests.Actions.GET_COMPLETED_STORIES:
                break;
            case ApiRequests.Actions.GET_INCOMPLETE_STORIES:
                System.out.println(
                    "************************************"
                    +"\nIncomplete Stories fetched from API.\n"
                    +"************************************"
                );

                // -- List that will contain all the Stories from the response.
                ArrayList<Story> incompleteStories = new ArrayList<>();

                // -- Get stories from response.
                try{
                    // -- Get JSON obj.
                    JSONObject obj  = new JSONObject(response);
                    JSONArray array = obj.optJSONArray("array");

                    /*
                     *
                     */
                    JSONObject row;
                    Story st; StoryDetails sd;
                    ArrayList<Sentence> se;
                    for (int i=0; i<array.length();i++){
                        row = array.getJSONObject(i);
                        sd = new StoryDetails(
                                row.getString("title"),
                                row.getString("theme"),
                                row.getString("main_character"));
                        se = new ArrayList<>();
                        se.add(new Sentence(row.getString("content")));
                        st = new Story(
                                row.getInt("id"),
                                sd, new User(row.getInt("creator_id")),
                                se, null
                        );
                        incompleteStories.add(st);
                        System.out.println(st);
                    }
                } catch(JSONException e){ e.printStackTrace(); }

                // -- Refresh StoryChooserActivity.
                ((StoryChooserActivity)activity)
                        .refreshStoriesList(incompleteStories);
                break;

            //----------------------------------------------------------------------
            /*
             * DEBUG related.
             */
            case ApiRequests.Actions.RESET_DATABASE:
                System.out.println(
                    "************************************"
                    +"\nDBHandler has been Reset on API.\n"
                    +"************************************"
                );
                break;
        }
    }
}
