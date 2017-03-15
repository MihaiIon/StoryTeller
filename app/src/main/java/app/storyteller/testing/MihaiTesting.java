package app.storyteller.testing;

import android.content.Context;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.api.Api;
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
    public static void testingProfile(){

        String fake_google_id = "123456";
        DBHandler.openConnection();

        if (DBHandler.profileExists(fake_google_id)){
            profile = DBHandler.getProfile(Integer.parseInt(fake_google_id));
        }

        else {
            profile = new Profile(
                -1, fake_google_id, "TEST", 60, // Tokens gift - First Time.
                "pathpath",
                new Timestamp(System.currentTimeMillis()),
                new ArrayList<Story>()
            );
            DBHandler.addProfile(profile);

        }

        System.out.println("*******"+profile.toString()+"***********");
        DBHandler.closeConnection();
    }


    /**
     * Story TESTING
     */
    public static void testingStory(){

        Story story;
        int story_id = 123;
        DBHandler.openConnection();
        System.out.println("******* NUMBER OF STORIES : " + DBHandler.getStoryListSize() + " ***********");

        if (DBHandler.storyExists(story_id)){
            story = DBHandler.getStory(story_id);
        }

        else {
            StoryDetails st = new StoryDetails("My title", Story.Themes.FUNNY, "Mihai");
            ArrayList<Sentence> list = new ArrayList<Sentence>();

            list.add(new Sentence(
                    3, profile,
                    "Once upon a time there was Gena giving bad notes.",
                    new Timestamp(System.currentTimeMillis())));

            list.add(new Sentence(
                    4, profile, "And Then he died.",
                    new Timestamp(System.currentTimeMillis())));

            story = new Story(story_id, st, profile, list, new Timestamp(System.currentTimeMillis()));

            DBHandler.addStory(story);
        }

        System.out.println("**********"+story.toString()+"************");
        System.out.println("******* NUMBER OF STORIES : " + DBHandler.getStoryListSize() + " ***********");

        DBHandler.closeConnection();
    }

    public static void testingApiCreateProfile(){
        Api.createProfile("123456789", "Mihai", "abcdefg");
    }
}
