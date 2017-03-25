package app.storyteller.api;

import org.json.JSONException;
import org.json.JSONObject;

import app.storyteller.models.Profile;

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
            json.put("key", Api.API_KEY);
            json.put("google_id", google_id);
            json.put("name", name);
            json.put("imageURL", imageURL);
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Request.Actions.CREATE_PROFILE, json, true);
    }


    /**
     * Update all the Information of the Profile "p".
     */
    public static Request updateProfile(Profile p){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("id", p.getId());
            json.put("name", p.getName());
            json.put("tokens", p.getTokens());
            json.put("imageURL", p.getImageURL());
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Request.Actions.UPDATE_PROFILE, json, true);
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
