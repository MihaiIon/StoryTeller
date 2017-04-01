package app.storyteller.testing;

import android.app.Activity;
import android.content.Context;
import android.support.v7.app.AppCompatActivity;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.MainActivity;
import app.storyteller.StoryChooserActivity;
import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.database.DBHandler;
import app.storyteller.models.Profile;
import app.storyteller.models.Sentence;
import app.storyteller.models.Story;
import app.storyteller.models.StoryDetails;

/**
 * Created by Mihai on 2017-03-10.
 */

public class MihaiTesting {


    private static Profile profile = null;

    /**
     * Profile TESTING
     */
    /*
    public static void testingProfile(){

        String fake_google_id = "123456";
        DBHandler.openConnection();
        System.out.println("************* Nb Profiles : "+ DBHandler.getProfileListSize() +"*****************");
        System.out.println("************* Nb Accounts : "+ DBHandler.getAccountListSize() +"*****************");
        if (DBHandler.profileExists(fake_google_id)){
            profile = DBHandler.getProfile(fake_google_id);
            System.out.println("******* GET Profile by google_id : "+profile.toString()+"***********");
            profile = DBHandler.getProfile(profile.getId());
            System.out.println("******* GET Profile by id : "+profile.toString()+"***********");
        }

        else {
            profile = new Profile(
                -1, fake_google_id, "TEST", 60, // Tokens gift - First Time.
                "pathpath",
                new Timestamp(System.currentTimeMillis()),
                new ArrayList<Story>()
            );
            DBHandler.createAccount(profile);
        }
        System.out.println("************* Nb Profiles : "+ DBHandler.getProfileListSize() +"*****************");
        System.out.println("************* Nb Accounts : "+ DBHandler.getAccountListSize() +"*****************");
        DBHandler.closeConnection();
    }
    */

    /**
     * Story TESTING
     */
    public static void testingStory(Context ctx){

        Story story;
        int story_id = 126;
        DBHandler.openConnection(ctx);
        System.out.println("******* NUMBER OF STORIES : " + DBHandler.getStoryListSize() + " ***********");
        System.out.println("******* STORY EXISTS : " + DBHandler.storyExists(story_id) + " ***********");

        if (DBHandler.storyExists(story_id)){
            story = DBHandler.getStory(story_id);
        }


            Profile p = new Profile(123,"213asd532165","DatProfileTho",3,"lfask",new Timestamp(System.currentTimeMillis()),new ArrayList<Story>());
            DBHandler.addProfile(p);

            Profile testProfile = DBHandler.getProfile(123);
            System.out.println("**************TEST PROFILE INFOS : " + testProfile.getId() + " " + testProfile.getGoogleId() + " " + testProfile.getName() + " etc*********");

            StoryDetails st = new StoryDetails("My title", Story.Themes.FUNNY, "Mihai");
            ArrayList<Sentence> list = new ArrayList<Sentence>();

            list.add(new Sentence(
                    3, testProfile,
                    "Once upon a time there was Gena giving bad notes.",
                    new Timestamp(System.currentTimeMillis())));

            list.add(new Sentence(
                    4, testProfile, "And Then he died.",
                    new Timestamp(System.currentTimeMillis())));

            story = new Story(story_id, st, testProfile, list, new Timestamp(System.currentTimeMillis()));

            DBHandler.addStory(story);

            System.out.println("******* NUMBER OF STORIES AFTER ADDSTORY : " + DBHandler.getStoryListSize() + " ***********");
            System.out.println("******* STORY EXISTS AFTER ADDSTORY: " + DBHandler.storyExists(story.getId()) + " ***********");

            ArrayList<Story> favs = new ArrayList<Story>();
            favs.add(story);
            DBHandler.addFavorite(127, story.getId());
            Profile p2 = new Profile(127,"213asd532165","DatProfileTho",3,"lfask",new Timestamp(System.currentTimeMillis()),favs);
            DBHandler.addProfile(p2);
            ArrayList<Integer> favorites = DBHandler.getFavorites(p2.getId());
            System.out.println("************************Favorite stories are: ");

            for(int i =0;i<favorites.size();i++){
                System.out.println(favorites.get(i) + " , " );
            }
            System.out.println("FAVS OVER****************************");





        System.out.println("**********"+story.toString()+"************");
        System.out.println("******* NUMBER OF STORIES : " + DBHandler.getStoryListSize() + " ***********");

        DBHandler.closeConnection();
    }

    public static void testingApiCreateStory(AppCompatActivity app){
        /*Api.executeRequest(ApiRequests.createStory(
                new StoryDetails("My first Story", "Horror", "Gena"),
                "One the day, I saw myself in the mirror and I was TRIVIAL"), app);
*/
        /*Api.executeRequest(ApiRequests.updateStory(
                35, "I did so much proofs that my hand went numb",
                false), app);

        Api.executeRequest(ApiRequests.updateStory(
                35, "That's when I decided to buy old cloths and become TRIVIAL",
                false), app);

        Api.executeRequest(ApiRequests.updateStory(
                35, "And so my life began",
                false), app);*/

        /*Api.executeRequest(ApiRequests.createStory(
                new StoryDetails("My Second Story", "Algos", "Gilles"),
                "I'm simply Trivial and my name is evident"), app);

        Api.executeRequest(ApiRequests.updateStory(
                36, "When I was a kid, I got sick from evidence and became a...",
                false), app);

        Api.executeRequest(ApiRequests.updateStory(
                36, "That day I was born, trivial?",
                false), app);

        Api.executeRequest(ApiRequests.updateStory(
                36, "Pas a ta place?",
                false), app);*/

        //Api.executeRequest(ApiRequests.lockStory(27),app); // Took from the API.
        //Api.executeRequest(ApiRequests.unlockStory(27),app); // Took from the API.
        //Api.executeRequest(ApiRequests.isStoryLocked(27),app); // Took from the API.
        //Api.executeRequest(ApiRequests.getIncompleteStories(),app);
    }
}
