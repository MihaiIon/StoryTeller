package app.storyteller;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

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

        if(Math.random() == 0){
            startActivity(new Intent(this, MainActivity.class));
        }

        else{
            startActivity(new Intent(this, sign_in_out.class));
        }
    }
}
