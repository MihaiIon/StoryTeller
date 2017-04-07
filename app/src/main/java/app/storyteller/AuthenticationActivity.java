package app.storyteller;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Window;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;

import app.storyteller.database.DBHandler;
import app.storyteller.manager.StoryTellerManager;

public class AuthenticationActivity extends AppCompatActivity {

    /**
     *
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.setContentView(R.layout.activity_authentication);
        if(StoryTellerManager.isConnectedToInternet(getApplicationContext()))
        {
            // -- Google and Local DB.
            setUpGoogleStuff();
            DBHandler.openConnection(getApplicationContext());

            /*
             * If there is no Profile in the Database, go to SignInActivity.
             */
            if (DBHandler.getProfileListSize() == 0) {
                DBHandler.closeConnection();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
            }

            /*
             * Else, connect to Google's API with the current Google Account logged and Fetch the
             * Profile that corresponds to the Google Account's ID in the local Database.
             *
             * If the Profile Exist, go straight to the MainActivity. Else, go to SignInActivity.
             */
            else signInAndProceed();
        }
        else{
            Toast.makeText(getApplicationContext(),"This Application needs an Internet Connection, please connect to the internet to continue", Toast.LENGTH_SHORT).show();
            System.out.println("SYSTEM IS NOT CONNECTED TO THE INTERNET");
            //Wait
            try{Thread.sleep(1000, 1);}
            catch(InterruptedException e){e.printStackTrace();}
            //Reload AuthenticationActivity
            //startActivity(new Intent(this, AuthenticationActivity.class));
        }
    }




    //----------------------------------------------------------------------------
    // Google

    /**
     * Connect to the Google API.
     */
    private void setUpGoogleStuff(){
        // -- Options.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        // -- Builder.
        GoogleApiClient.Builder builder = new GoogleApiClient.Builder(this).enableAutoManage(
                this, new GoogleApiClient.OnConnectionFailedListener() {
                    @Override public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                        Toast.makeText(getApplicationContext(),
                                "Sorry there was an error with your connection",
                                Toast.LENGTH_SHORT).show();
                    }});

        // -- Set Client.
        StoryTellerManager.init(builder.addApi(Auth.GOOGLE_SIGN_IN_API, gso).build());
    }


    //----------------------------------------------------------------------------
    // Sign In Process

    /**
     * Sign In to the Google's API.
     */
    private void signInAndProceed(){
        Intent signInIntent = Auth.GoogleSignInApi
                .getSignInIntent(StoryTellerManager.getGoogleApiClient());
        startActivityForResult(signInIntent, 1);
    }

    /**
     *
     *
     * @param requestCode   : TODO.
     * @param resultCode    : TODO.
     * @param data          : TODO.
     */
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // -- If requestCode == 1, the request has succeeded.
        if (requestCode == 1) {

            // -- Get results.
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                // -- Get Account Infos.
                GoogleSignInAccount acct = result.getSignInAccount();

                /*
                 * Check if the current Google Account has a Profile on this Device and
                 * if he/she was signed-in the last time he/she used the app : Login and proceed
                 * to MainActivity.
                 */
                if (DBHandler.accountExists(acct.getId())&& true /*TODO*/)
                {
                    DBHandler.closeConnection();
                    Intent i = new Intent(this, LoadProfileActivity.class);
                    i.putExtra("is_account_in_database", true);
                    i.putExtra("account_id", acct.getId());
                    startActivity(i);
                    finish();
                }

                // -- Proceed to SignInActivity.
                else
                {
                    DBHandler.closeConnection();
                    startActivity(new Intent(this, SignInActivity.class));
                    finish();
                }
            } else System.out.println("****ERROR******AN ERROR IS FUCKING ME UP (Most likely error: 12501");
        }else System.out.println("****ERROR******REQUEST CODE FAILED, requestCode : " + requestCode);
    }


    /**
     * Close app.
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
    }
}