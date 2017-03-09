package app.storyteller;
/**
 * Created by farlyprj on 17-03-08.
 */
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.GoogleApiClient;

public class sign_in_out extends AppCompatActivity {

    /*
    * Sign contains all for first time log in and for later log out functionality
    *
    * Sign_in: does at it says; sends data to IN server for it to be added
    *  ***Note: Will need back-end verification in BD for no extra add, and connectivity
    *
    * Sign_out: differentiates user's account from device.
    *           (Will need to verify that Google ID sent is indeed in the BD)
    */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_out);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).build();

        final GoogleApiClient mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        final Button button = (Button) findViewById(R.id.Sign_In_button);
        button.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
                startActivityForResult(signInIntent, 123465/*RC_SIGN_IN*/);
            }

        });





    }
}
