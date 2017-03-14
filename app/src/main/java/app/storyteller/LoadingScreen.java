package app.storyteller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import java.sql.Timestamp;
import java.util.ArrayList;

import app.storyteller.database.DBHandler;
import app.storyteller.models.Profile;
import app.storyteller.models.Story;
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

        DBHandler.openConnection(getApplicationContext());
        /* TESTING -- Mihai -- TESTING */
        //MihaiTesting.testingProfile(getApplicationContext());
        //MihaiTesting.testingStory(getApplicationContext());
        /* TESTING -- Mihai -- TESTING */

        if(DBHandler.profileExists()){
            DBHandler.closeConnection();
            startActivity(new Intent(this, MainActivity.class));
        }

        else /*if(Internet is active)*/{
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
