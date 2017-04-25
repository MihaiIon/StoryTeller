package app.storyteller.fragments.dialogs;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
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
import app.storyteller.manager.AppManager;

/**
 * Created by Mihai on 2017-01-28.
 */
public class Settings extends DialogFragment {

    /*
     *
     */
    private GoogleApiClient mGoogleApiClient;

    /*
     *
     */
    private View layout;

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
        mGoogleApiClient = AppManager.getGoogleApiClient(getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.dialog_settings, container, false);

        // Add listeners to buttons.
        //initializeClearDBBtn(view.findViewById(R.id.settings_clearDB_btn));
        initializeTokenBtns();
        initializeCloseBtn();
        initializeLogOutBtn();

        // Allow closing on outside press
        getDialog().setCanceledOnTouchOutside(true);
        return layout;
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


    /*
     * Provides a way to
     *
    private int count;

    /**
     * -- DEBUG TOOL --
     *
     * Resets the database on the server. Useful when the app's data structure has been
     * changed or is not in sync with the server's database.
     *
    private void initializeClearDBBtn(View view){
        count = 0;
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count) {
                    case 0:
                        Toast.makeText(getContext(), getString(R.string.setting_are_you_sure), Toast.LENGTH_SHORT).show();
                        count++;
                        break;
                    case 1:
                        Toast.makeText(getContext(), getString(R.string.setting_dbh_cleared), Toast.LENGTH_SHORT).show();
                        // Api.resetDatabase();
                        count++;
                        break;
                    default:
                        Toast.makeText(getContext(), getString(R.string.setting_dbh_already_cleared), Toast.LENGTH_SHORT).show();
                }
            }
        });
    }*/

    /**
     * -- DEBUG TOOL --
     *
     *  Adds tokens to the current User.
     */
    private void initializeTokenBtns(){
        layout.findViewById(R.id.settings_grant_token_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getTokenManager().grantToken(getActivity());
                Toast.makeText(getContext(), getString(R.string.settings_grant_tokens), Toast.LENGTH_SHORT).show();
            }
        });
        layout.findViewById(R.id.settings_consume_token_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppManager.getTokenManager().consumeToken(getActivity());
                Toast.makeText(getContext(), getString(R.string.settings_consume_tokens), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initializeLogOutBtn()
    {
        Button btn = (Button) layout.findViewById(R.id.setting_log_out_button);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                        new ResultCallback<Status>() {
                            @Override
                            public void onResult(Status status) {
                                Intent i = new Intent(getContext(), AuthenticationActivity.class);
                                i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(i);
                            }});}
        });
    }

    private void initializeCloseBtn(){
        Button btn = (Button) layout.findViewById(R.id.settings_close_btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) { dismiss(); }});
    }
}
