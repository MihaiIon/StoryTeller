package app.storyteller;

import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Toast;

import java.util.ArrayList;

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
        DBHandler.openConnection(getApplicationContext());

        if (DBHandler.profileExists(123456)){
            p = DBHandler.getProfile(123456);
            //Toast.makeText(getApplicationContext(), "PROFILE LOADED : ", Toast.LENGTH_LONG);
            System.out.println("*******"+p.toString()+"***********");
        }

        else {
            p = new Profile(123456, "TEST", 255, "pathpath", "321654", new ArrayList<Stories>());
            DBHandler.addToProfile(p);
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
