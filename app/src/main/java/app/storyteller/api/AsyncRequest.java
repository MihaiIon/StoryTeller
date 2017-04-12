package app.storyteller.api;

import android.app.Activity;
import android.os.AsyncTask;
import android.support.v4.app.Fragment;

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

import app.storyteller.LoadProfileActivity;
import app.storyteller.StoryChooserActivity;
import app.storyteller.StoryEditorActivity;
import app.storyteller.database.DBHandler;
import app.storyteller.fragments.MainAllStoriesFragment;
import app.storyteller.manager.AppManager;
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
    private Activity activity;

    /**
     * Current activity that ordered a AsyncRequest.
     */
    private Fragment fragment;
    /**
     * The resquest sent to the API.
     */
    private Request request;

    /**
     * Constructors
     */
    AsyncRequest(Request request, Activity activity){
        this.request = request;
        this.activity = activity;
        this.fragment = null;
        execute();
    }

    AsyncRequest(Request request, Fragment fragment){
        this.request = request;
        this.activity = null;
        this.fragment = fragment;
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
                processProfile(response);
                if (activity instanceof StoryEditorActivity)
                    activity.finish();
                break;

            //----------------------------------------------------------------------
            /*
             * Story related.
             */
            case ApiRequests.Actions.CREATE_STORY:
            case ApiRequests.Actions.UPDATE_STORY:
            case ApiRequests.Actions.LOCK_STORY:
            case ApiRequests.Actions.UNLOCK_STORY:
                break;
            case ApiRequests.Actions.IS_STORY_LOCKED:
                processIsStoryLocked(response);
                break;
            case ApiRequests.Actions.GET_STORY_COMPLETION_STATE:
                processGetStoryCompletionState(response);
                break;


            //----------------------------------------------------------------------
            /*
             * Story related - Fetches
             */
            case ApiRequests.Actions.GET_COMPLETED_STORIES:
                processCompletedStories(response);
                break;
            case ApiRequests.Actions.GET_INCOMPLETE_STORIES:
                processIncompleteStories(response);
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



    //----------------------------------------------------------------------


    /**
     *
     * @param response : Response from API.
     */
    private void processProfile(String response){
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
            AppManager.setAccountManager(acc);
            System.out.println(acc);

            // -- Add Account in local DB.
            DBHandler.openConnection(activity.getApplicationContext());
            if (request.getAction().equals(ApiRequests.Actions.CREATE_PROFILE)){
                System.out.println(
                    "************************************"
                    +"\nProfile Created on API.\n"
                    +"************************************"
                );
                DBHandler.createAccount(acc);
                DBHandler.closeConnection();
                ((LoadProfileActivity)activity).onAccountCreated(); // Proceed to MainActivity.
            }

            // -- Add/Update Account in local DB.
            else {
                System.out.println(
                    "************************************"
                    +"\nProfile Updated on API.\n"
                    +"************************************"
                );
                DBHandler.updateAccount(acc);
                DBHandler.closeConnection();
            }

        } catch(JSONException e){ e.printStackTrace(); }
    }


    /**
     *
     * @param response : Response from API.
     */
    private void processCompletedStories(String response){
        System.out.println(
                "************************************"
                +"\nComplete Stories fetched from API.\n"
                +"************************************"
        );

        // -- List that will contain all the Stories from the response.
        ArrayList<Story> completeStories = new ArrayList<>();

        // -- Get stories from response.
        try{
            // -- Get JSON obj.
            JSONArray storyArr  = new JSONArray(response);

            /*
             *
             */
            JSONObject row;
            JSONArray sentenceArr;
            JSONObject storyObj; JSONObject sentenceObj;
            Story st; StoryDetails sd;
            ArrayList<Sentence> se;
            for (int i=0; i<storyArr.length();i++)
            {
                // --
                row = storyArr.getJSONObject(i);

                // --
                se = new ArrayList<>();
                sentenceArr = row.getJSONArray("content");
                for (int j=0; j<sentenceArr.length();j++)
                {
                    sentenceObj = sentenceArr.getJSONObject(j);
                    se.add(new Sentence(
                            sentenceObj.getInt("id"),
                            new User(sentenceObj.getInt("author_id")),
                            sentenceObj.getString("content"),
                            Timestamp.valueOf(sentenceObj.getString("creation_date"))));
                }

                // -- Get story information.
                storyObj = row.getJSONObject("story");

                // --
                sd = new StoryDetails(
                        storyObj.getString("title"),
                        storyObj.getString("theme"),
                        storyObj.getString("main_character"));

                st = new Story(
                        storyObj.getInt("id"),
                        sd, new User(storyObj.getInt("creator_id")), se,
                        Timestamp.valueOf(storyObj.getString("creation_date")));

                completeStories.add(st);
                System.out.println(st);
            }
        } catch(JSONException e){ e.printStackTrace(); }

        // -- Refresh.
        ((MainAllStoriesFragment)fragment)
                .onCompletedStoriesFetched(completeStories);
    }

    /**
     *
     * @param response : Response from API.
     */
    private void processIncompleteStories(String response){
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
    }


    /**
     *
     * @param response : Response from API.
     */
    private void processIsStoryLocked(String response){
        try{
            JSONObject obj = new JSONObject(response);
            ((StoryChooserActivity)activity)
                    .onItemVerified(obj.getInt("value") == 1);
        }catch(JSONException e){ e.printStackTrace(); }
    }

    /**
     *
     * @param response : Response from API.
     */
    private void processGetStoryCompletionState(String response){
        try{
            JSONObject obj = new JSONObject(response);
            ((StoryEditorActivity)activity)
                    .onStoryCompletionResult(obj.getInt("value"));
        }catch(JSONException e){ e.printStackTrace(); }
    }
}
