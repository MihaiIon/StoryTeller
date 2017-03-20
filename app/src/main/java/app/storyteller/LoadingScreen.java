package app.storyteller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import app.storyteller.database.DBHandler;
import app.storyteller.manager.StoryTellerManager;
import app.storyteller.testing.MihaiTesting;

public class LoadingScreen extends AppCompatActivity {
/*
* loading screen is the true main file as it verifies if there is an account in the local
* data base and progresses app to appropriate location:
*
*   if there is an account: goes to main
*   if there isnt: goes to sign_in_out
* */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        StoryTellerManager.init(getApplicationContext(), "123123");
        //DBHandler.openConnection();
        /* TESTING -- Mihai -- TESTING */
        //MihaiTesting.testingProfile(getApplicationContext());
       // MihaiTesting.testingStory();
        //MihaiTesting.testingApiCreateProfile();
        /* TESTING -- Mihai -- TESTING */
            DBHandler.openConnection();
        if(DBHandler.profileExists("asdasda")){
            DBHandler.closeConnection();
            startActivity(new Intent(this, MainActivity.class));
        }

        else if(StoryTellerManager.isConnected()){
            DBHandler.closeConnection();
            startActivity(new Intent(this, SignInActivity.class));
        }
        /*else
        {
            MAKE FU ACTIVITY ACTIVE (SORRY NOT SORRY)
            YOU WILL BE TRACKED AND KILLED
        }*/
    }
}
