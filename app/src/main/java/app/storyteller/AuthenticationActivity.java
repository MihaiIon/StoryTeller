package app.storyteller;

import android.accounts.Account;
import android.accounts.AccountManager;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import java.util.LinkedList;
import java.util.List;

import app.storyteller.database.DBHandler;
import app.storyteller.manager.StoryTellerManager;
import app.storyteller.testing.MihaiTesting;

public class AuthenticationActivity extends AppCompatActivity {
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
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);

        // --
        //DBHandler.openConnection(getApplicationContext());

        /* TESTING -- Mihai -- TESTING */
        //MihaiTesting.testingProfile(getApplicationContext());
        //MihaiTesting.testingStory();
        //MihaiTesting.testingApiCreateProfile();
        /* TESTING -- Mihai -- TESTING */
        //retrieveMainAccountFromDevice();
        if(false){
            //DBHandler.closeConnection();
            startActivity(new Intent(this, MainActivity.class));
        }

        else{
            //DBHandler.closeConnection();
            startActivity(new Intent(this, SignInActivity.class));
        }
    }


    /**
     *
     * @return
     */
    private void retrieveMainAccountFromDevice(){
        AccountManager manager = (AccountManager) getSystemService(ACCOUNT_SERVICE);;
        Account[] accounts = manager.getAccounts();
        Toast.makeText(this, accounts.length+"", Toast.LENGTH_SHORT).show();
        for (Account account : accounts) {
            Toast.makeText(this, account.type, Toast.LENGTH_SHORT).show();
        }

        /*if (!possibleEmails.isEmpty() && possibleEmails.get(0) != null) {
            String email = possibleEmails.get(0);
            String[] parts = email.split("@");
            if (parts.length > 0 && parts[0] != null)
                return parts[0];
            else
                return null;
        } else
            return null;*/
    }
}
