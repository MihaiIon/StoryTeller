package app.storyteller;
/**
 * Created by farlyprj on 17-03-08.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import app.storyteller.manager.StoryTellerManager;

public class SignInActivity extends AppCompatActivity {

    /**
     * Triggers to Google Sign-in process.
     */
    private Button googleSignInBtn;

    /**
     * TODO.
     */
    private Button googleNoAccountBtn;

    /**
     * ~DEBUG~
     * -- Skips the Google authentication process.
     */
    private Button skipToMainBtn;


    /**
     * On create :
     * -- Note: Will need back-end verification in BD for no extra add, and connectivity
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // -- General.
        this.requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_sign_in);
        setUpButtons();
    }




    //----------------------------------------------------------------------------
    // Methods

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
        if (requestCode == 1) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);

            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();

                /*
                 * Launch LoadingActivity and wait for the API response. When
                 * the application is ready. proceed to MainActivity.
                 */
                Intent intent = new Intent(this, LoadingActivity.class);
                intent.putExtra("next_activity", LoadingActivity.ActivityList.MAIN_ACTIVITY);
                intent.putExtra("account_id", acct.getId());
                intent.putExtra("account_name", "ééééééééééé"/*acct.getDisplayName()*/);
                intent.putExtra("account_image_url", acct.getPhotoUrl().toString());

                // --
                startActivity(intent);
                finish();
            } else System.out.println("AN ERROR IS FUCKING ME UP (Most likely error: 12501");
        }
    }



    // int statusCode = result.getStatus().getStatusCode();
    //System.out.println(statusCode);

    //System.out.println("Account is: "+acct.getDisplayName() +" has id: " + acct.getId()+ " and has email: "+ acct.getEmail()+ "Photo URL: " + acct.getPhotoUrl());
    //Toast.makeText(getApplicationContext(),"Account is: "+acct.getDisplayName(), Toast.LENGTH_SHORT).show();
    //Toast.makeText(getApplicationContext(), "has id: " + acct.getId(), Toast.LENGTH_SHORT).show();
    //Toast.makeText(getApplicationContext()," and has email: "+ acct.getEmail(), Toast.LENGTH_SHORT).show();



    //----------------------------------------------------------------------------
    // Helpers

    /**
     * Sets up the buttons functionality and behaviour.
     */
    private void setUpButtons() {

        // -- Triggers Sign-in process.
        googleSignInBtn = (Button) findViewById(R.id.google_sign_in_btn);
        googleSignInBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(
                        StoryTellerManager.getGoogleApiClient());
                startActivityForResult(signInIntent, 1);
                // On complete goes to -> onActivityResult().
            }
        });

        // -- TODO.
        // -> No Idea.
        googleNoAccountBtn = (Button) findViewById(R.id.google_no_account_btn);
        googleNoAccountBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        // -- DEBUG --
        // Skips the Google authentication process.
        skipToMainBtn = (Button)findViewById(R.id.skip_to_main_btn);
        skipToMainBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), MainActivity.class));
            }
        });
    }
}