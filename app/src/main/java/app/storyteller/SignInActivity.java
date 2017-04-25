package app.storyteller;
/**
 * Created by farlyprj on 17-03-08.
 */
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import app.storyteller.manager.AppManager;

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
            Log.d("Code", ""+requestCode);
            Log.d("Code", ""+resultCode);
            if (result.isSuccess()) {
                GoogleSignInAccount acct = result.getSignInAccount();

                /*
                 * Launch LoadProfileActivity and wait for the API response. When
                 * the application is ready. proceed to MainActivity.
                 */
                Intent intent = new Intent(this, LoadProfileActivity.class);
                intent.putExtra("account_id", acct.getId());
                intent.putExtra("account_name", acct.getDisplayName());
                intent.putExtra("account_image_url", acct.getPhotoUrl().toString());

                // --
                startActivity(intent);
                finish();
            } else System.out.println("AN ERROR IS FUCKING ME UP (Most likely error: 12501");
        }
    }


    /**
     * Close app.
     */
    @Override
    public void onBackPressed() {
        finishAffinity();
    }


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
                startActivityForResult(Auth.GoogleSignInApi.getSignInIntent(
                        AppManager.getGoogleApiClient(getApplicationContext())), 1);
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
    }
}