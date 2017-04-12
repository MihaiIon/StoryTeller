package app.storyteller.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;


import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.ToggleButton;

import app.storyteller.R;
import app.storyteller.StoryChooserActivity;
import app.storyteller.fragments.dialogs.Settings;
import app.storyteller.manager.AppManager;


/**
 * Created by Mihai on 2017-01-20.
 */
public class MainHomeFragment extends Fragment /*implements View.OnClickListener*/ {

    /**
     * The current fragment layout.
     */
    private ViewGroup fragmentLayout;

    /**
     * Settings button.
     */
    private ImageButton settings;

    /**
     * Profile Views.
     */
    private ImageView profileImage;
    private TextView profileName;

    /**
     * Tokens Related.
     */
    private TextView timerText;
    private ToggleButton token1,token2,token3;

    /**
     * Central button that triggers the StoryChooserActivity.
     */
    private ImageView playBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        initHeader();
        initPlayBtn();

        return fragmentLayout;
    }

    //-----------------------------------------------------------------------------------
    // Init


    /**
     *
     */
    private void initHeader(){
        profileImage = (ImageView)fragmentLayout.findViewById(R.id.profile_image_view);
        profileImage.setImageBitmap(AppManager.getAccountManager().getAccountImage());
        profileName  = (TextView)fragmentLayout.findViewById(R.id.profile_name_text_view);
        profileName.setText(AppManager.getAccount().getName());
        initTokens();
        initSettings();
    }

    /**
     * The main settings of the app.
     */
    private void initSettings() {
        settings = (ImageButton)fragmentLayout.findViewById(R.id.settings_btn);
        settings.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) { ft.remove(prev); }
                ft.addToBackStack(null);

                // Create and show the dialog.
                Settings s = Settings.newInstance();
                s.show(ft, "title");
            }
        });
    }

    //-------------------------------------------------------------------
    // Tokens

    /**
     *
     */
    private void initTokens() {
        timerText = (TextView) fragmentLayout.findViewById(R.id.timerText);
        token1 = (ToggleButton) fragmentLayout.findViewById(R.id.token1);
        token2 = (ToggleButton) fragmentLayout.findViewById(R.id.token2);
        token3 = (ToggleButton) fragmentLayout.findViewById(R.id.token3);
        updateTokensUI(AppManager.getTokenManager().getTokens());
    }

    /**
     * This methods needs to be called when there is a need to
     * @param value : Number of available tokens.
     */
    public void updateTokensUI(int value) {
        switch (value){
            case 3:
                token1.setChecked(true);
                token2.setChecked(true);
                token3.setChecked(true);
                break;
            case 2:
                token1.setChecked(false);
                token2.setChecked(true);
                token3.setChecked(true);
                break;
            case 1:
                token1.setChecked(false);
                token2.setChecked(false);
                token3.setChecked(true);
                break;
            default:
                token1.setChecked(false);
                token2.setChecked(false);
                token3.setChecked(false);
        }
    }


    //-----------------------------------------------------------------------------------
    // Play Btn

    private void initPlayBtn(){
        playBtn = (ImageView)fragmentLayout.findViewById(R.id.home_play_btn);
        playBtn.setBackgroundResource(R.drawable.play_btn_animation);
        ((AnimationDrawable) playBtn.getBackground()).start();
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), StoryChooserActivity.class));
            }
        });
    }
}
