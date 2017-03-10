package app.storyteller;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;

import app.storyteller.database.DBHandler;
import app.storyteller.models.Profile;
import app.storyteller.models.Stories;

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

        Profile p;
        int fake_google_id = 123456;
        DBHandler.openConnection(getApplicationContext());

        if (DBHandler.profileExists(fake_google_id)){
            p = DBHandler.getProfile(fake_google_id);
            System.out.println("*******"+p.toString()+"***********");
        }

        else {
            p = new Profile(
                fake_google_id,
                "TEST",
                60,         // Tokens gift - First Time.
                "pathpath",
                new Timestamp(System.currentTimeMillis()),
                new ArrayList<Stories>()
            );
            DBHandler.addToProfiles(p);
            System.out.println("*******"+p.toString()+"***********");
        }

        DBHandler.closeConnection();

        /*if(Math.random() == 0){
            startActivity(new Intent(this, MainActivity.class));
        }

        else{
            startActivity(new Intent(this, sign_in_out.class));
        }*/
    }
}
