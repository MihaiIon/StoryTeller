package app.storyteller.api;

import android.content.Context;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Mihai on 2017-03-22.
 */

public class ApiRequests {



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
    public static Request createProfile(String google_id, String name, String imageURL){
        JSONObject json = new JSONObject();
        try {
            json.put("google_id", google_id);
            json.put("name", name);
            json.put("imageURL", imageURL);
        } catch (JSONException e) { e.printStackTrace(); }

        String[] params = new String[]{ Api.API_KEY, json.toString()};
        return new Request(Request.Actions.CREATE_PROFILE, params, true);
    }






}
