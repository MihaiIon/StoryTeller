package app.storyteller.api;

import org.json.JSONException;
import org.json.JSONObject;

import app.storyteller.manager.StoryTellerManager;
import app.storyteller.models.Account;
import app.storyteller.models.Profile;
import app.storyteller.models.Sentence;
import app.storyteller.models.StoryDetails;

/**
 * Created by Mihai on 2017-03-22.
 */

public class ApiRequests {


    /**
     * TODO.
     */
    final static class Actions{
        final static String CREATE_PROFILE = "createprofile";
        final static String UPDATE_PROFILE = "updateprofile";

        final static String CREATE_STORY   = "createstory";
        final static String UPDATE_STORY   = "updatestory";

        final static String LOCK_STORY      = "lockstory";
        final static String UNLOCK_STORY    = "unlockstory";
        final static String IS_STORY_LOCKED = "isstorylocked";
        final static String GET_STORY_COMPLETION_STATE = "getstorycompletionstate";

        final static String GET_COMPLETED_STORIES   = "getcompletedstories";
        final static String GET_INCOMPLETE_STORIES = "getincompletestories";

        final static String RESET_DATABASE = "resetdatabase";
    }


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
        return new Request(Actions.CREATE_PROFILE, json);
    }


    /**
     * Update all the Information of the current Profile.
     */
    public static Request updateProfile(){
        Account acc = StoryTellerManager.getAccount();
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("id", acc.getId());
            json.put("name", acc.getName());
            json.put("tokens", acc.getTokens());
            json.put("imageURL", acc.getImageURL());
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.UPDATE_PROFILE, json);
    }


    /********************************************
     *											*
     *											*
     *	Story Related							*
     *											*
     *											*
     ********************************************/

    /**
     * Update all the Information of the Profile "p".
     */
    public static Request createStory(StoryDetails sd, String sentence){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("title", sd.getTitle());
            json.put("theme", sd.getTheme());
            json.put("sentence_content", sentence);
            json.put("character_name", sd.getMainCharacter());
            json.put("creator_id", StoryTellerManager.getAccount().getId());
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.CREATE_STORY, json);
    }

    /**
     * Update all the Information of the Profile "p".
     */
    public static Request updateStory(int story_id, String sentence, boolean isCompleted){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("story_id", story_id);
            json.put("sentence_content", sentence);
            json.put("author_id", StoryTellerManager.getAccount().getId());
            json.put("is_completed", isCompleted ? 1 : 0);
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.UPDATE_STORY, json);
    }

    /**
     * Provides the number of sentences in the current story.
     */
    public static Request getStoryCompletionState(int story_id){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("story_id", story_id);
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.GET_STORY_COMPLETION_STATE, json);
    }

    /**
     * To make sure that no story is edited by multiple users, each
     * story must be locked when in use by a user.
     */
    public static Request lockStory(int story_id){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("story_id", story_id);
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.LOCK_STORY, json);
    }

    /**
     * When the user finished editing the story, the story must be
     * unlock in order.
     */
    public static Request unlockStory(int story_id){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("story_id", story_id);
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.UNLOCK_STORY, json);
    }

    /**
     * Checks if the current selected story is in use.
     */
    public static Request isStoryLocked(int story_id){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("story_id", story_id);
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.IS_STORY_LOCKED, json);
    }

    /**
     * Provides a list of completed stories related to the current
     * Account/Profile.
     */
    public static Request getCompletedStories(){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("profile_id", StoryTellerManager.getAccount().getId());
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.GET_COMPLETED_STORIES, json);
    }

    /**
     * Provides a list of incomplete stories from the API which the
     * current Account didn't participated as the last author.
     */
    public static Request getIncompleteStories(){
        JSONObject json = new JSONObject();
        try {
            json.put("key", Api.API_KEY);
            json.put("profile_id", StoryTellerManager.getAccount().getId());
        } catch (JSONException e) { e.printStackTrace(); }
        return new Request(Actions.GET_INCOMPLETE_STORIES, json);
    }



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
