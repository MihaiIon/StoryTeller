package app.storyteller;
/**
 * Created by farlyprj on 17-03-08.
 */
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import app.storyteller.R;

public class SignInActivity extends AppCompatActivity {

    /*
    * Sign contains all for first time log in and for later log out functionality
    *
    * (RC_Sign_in = 1) Sign_in: does at it says; sends data to IN server for it to be added
    *  ***Note: Will need back-end verification in BD for no extra add, and connectivity
    *
    * (RC_Sign_out = 2 ) Sign_out: differentiates user's account from device.
    *           (Will need to verify that Google ID sent is indeed in the BD)
    */

    //TODO: put a verificator of logged in already for sign in, and a if logged in for sign out

    private GoogleApiClient mGoogleApiClient;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_out);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this,
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                //try again? Show message of error and try again?
                            }})
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

        System.out.println("I'm about to load the button");

        /**
         * Buttons
         */

        final SignInButton signInButton = (SignInButton) findViewById(R.id.sign_in_button);
        signInButton.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                System.out.println("SIGN IN STARTS HERE");
                signIn();
            }


        });

       // ------------------------------
        //Temp sign out to be put in the place where sign out button is as well as the gso maker
        //and  other things needed
        final Button signOutButton = (Button) findViewById(R.id.tempSignOut_button);
        signOutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                System.out.println("SIGN OUT STARTS HERE");
                LogOut();
            }
        });

/**
 * Sign Methods
 */

        //TODO: Sign in result (in activityResult should start main activity
    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, 1);
        System.out.println("IM HERE BITCH");
    }

    //TODO: LogOut needs a :"ARE you sure you want to log out?" extra step and call a clear local database function

    private void LogOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(           //launches google sign out and resets sign in process
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getApplicationContext(),"Logged Out Completed", Toast.LENGTH_SHORT).show();
                        Intent FreshStart = new Intent(getApplicationContext(),LoadingScreen.class);
                        startActivity(FreshStart);
                    }
                });

    }

        //function that utilises result from action button to do the next things...
        // (like get id and send it to BD)

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        System.out.println("Account id is being treated");

        // Result returned from launching the Intent from
        //   GoogleSignInApi.getSignInIntent(...);

        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            // int statusCode = result.getStatus().getStatusCode();
            //System.out.println(statusCode);

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();
                // Get account information
                System.out.println("Account is: "+acct.getDisplayName() +" has id: " + acct.getId()+ " and has email: "+ acct.getEmail()+ "Photo URL: " + acct.getPhotoUrl());
                //String mFullName = acct.getDisplayName();

            } else System.out.println("AN ERROR IS FUCKING ME UP");
        }
    }

}

