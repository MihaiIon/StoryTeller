package app.storyteller.fragments.dialogs;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v4.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import app.storyteller.AuthenticationActivity;
import app.storyteller.R;
import app.storyteller.database.DBHandler;

/**
 * Created by Mihai on 2017-01-28.
 */
public class Settings extends DialogFragment {

    //Local variable
    private GoogleApiClient mGoogleApiClient;

    /**
     * Creates a new instance of the Settings Dialog.
     */
    public static Settings newInstance() {
        Settings settings = new Settings();
        Bundle args = new Bundle();
        settings.setArguments(args);
        return settings;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // Here we choose and set the style of our dialog.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);

        /*
            We set up for log out
             NOTE: Sign Out needs sign in options from google to work? weird but it works
             DATABASE CLEAR IS NOT DONE, ONLY NEEDS TO BE INSERTED BEFORE activityStart
         */

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(this.getResources().getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(getActivity().getApplicationContext())
                .enableAutoManage(getActivity(),
                        new GoogleApiClient.OnConnectionFailedListener() {
                            @Override
                            public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
                                //try again? Show message of error and try again?
                            }})
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_settings, container, false);

        // Add listeners to buttons.
        initializeClearDBBtn(view.findViewById(R.id.settings_clearDB_btn));
        initializeTokensBtn(view.findViewById(R.id.settings_addTokens_btn));
        initializeExitBtn(view.findViewById(R.id.settings_exit_btn));
        initializeLogOutBtn(view.findViewById(R.id.setting_log_out_button));

        return view;
    }

    @Override
    public void onResume()
    {
        super.onResume();
        int width = getResources().getDimensionPixelSize(R.dimen.settings_dialog_width);
        Window window = getDialog().getWindow();
        window.setLayout(width, FrameLayout.LayoutParams.WRAP_CONTENT);
        window.setGravity(Gravity.CENTER);
        //TODO:
    }



    //------------------------------------------------------------------------------------------



    /**
     * Provides a way to
     */
    private int count;

    /**
     * -- DEBUG TOOL --
     *
     * Resets the database on the server. Useful when the app's data structure has been
     * changed or is not in sync with the server's database.
     */
    private void initializeClearDBBtn(View view){
        count = 0;
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count) {
                    case 0:
                        Toast.makeText(getContext(), "Are you sure?", Toast.LENGTH_SHORT).show();
                        count++;
                        break;
                    case 1:
                        Toast.makeText(getContext(), "DBHandler cleared.", Toast.LENGTH_SHORT).show();
                        // Api.resetDatabase();
                        count++;
                        break;
                    default:
                        Toast.makeText(getContext(), "DBHandler already cleared.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * -- DEBUG TOOL --
     *
     *  Adds tokens to the current User.
     */
    private void initializeTokensBtn(View view){
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "100 Tokens added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * -- DEBUG TOOL --
     *
     * When the "Exit" button is pressed, the dialog is dismissed.
     */
    private void initializeExitBtn(View view){
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //resets gso and mGoogleApiClient
                closeConnection();
                //closes popup
                dismiss();
            }
        });
    }

    /**
     * NOT DEBUG (maybe)
     *
     * When the "Log out" button is clicked, the account is unlinked and
     */

    /*
    DATABASE CLEAR IS NOT DONE, ONLY NEEDS TO BE INSERTED BEFORE StartActivity(...)
     */
    private void initializeLogOutBtn(View view)
    {

        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logOut();
            }
        });
    }

    private void logOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(           //launches google sign out and resets sign in process
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        Toast.makeText(getContext(), "Logging out", Toast.LENGTH_SHORT).show();
                        //Setting has account to false
                        DBHandler.openConnection(getContext());
                        //Make current account not
                        DBHandler.closeConnection();
                        //Setting up new Loading screen
                        Intent FreshStart = new Intent(getContext(),AuthenticationActivity.class);
                        startActivity(FreshStart);
                    }
                });

    }

    private void closeConnection() {
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
