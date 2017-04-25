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

import app.storyteller.api.GoogleApiHelper;
import app.storyteller.database.DBHandler;
import app.storyteller.manager.AppManager;

public class AuthenticationActivity extends AppCompatActivity {

    /**
     *
     */
    private GoogleApiClient googleApiClient;

    /**
     *
     *
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.setContentView(R.layout.activity_authentication);
        if(AppManager.isConnectedToInternet(getApplicationContext()))
        {
            AppManager.init();
            googleApiClient = AppManager.getGoogleApiClient(getApplicationContext());
            DBHandler.openConnection(getApplicationContext());

            /*
             * If there is no Profile in the Database, go to SignInActivity.
             */
            if (DBHandler.getProfileListSize() == 0) {
                DBHandler.closeConnection();
                startActivity(new Intent(this, SignInActivity.class));
                finish();
            } else signInAndProceed();
        }
        else{
            Toast.makeText(
                    getApplicationContext(),
                    getString(R.string.authentification_need_internet),
                    Toast.LENGTH_LONG).show();
            System.exit(0);
        }
    }


    //----------------------------------------------------------------------------
    // Sign In Process

    /**
     * Sign In to the Google's API.
     */
    private void signInAndProceed(){
        startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(googleApiClient), 1);
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
        /*
         * Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...).
         */
        if (requestCode == 1)
        {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess())
            {
                // -- Get Account Info.
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