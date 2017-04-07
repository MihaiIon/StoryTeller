package app.storyteller.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import app.storyteller.R;
import app.storyteller.StoryChooserActivity;
import app.storyteller.fragments.dialogs.Settings;
import app.storyteller.manager.StoryTellerManager;

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
// initializeTokens(home.findViewById(R.id.token1), home.findViewById(R.id.token2),home.findViewById(R.id.token3));


    //-----------------------------------------------------------------------------------
    // Init

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
                if (prev != null) {
                    ft.remove(prev);
                }
                ft.addToBackStack(null);

                // Create and show the dialog.
                Settings s = Settings.newInstance();
                s.show(ft, "title");
            }
        });
    }

    /**
     *
     */
    private void initHeader(){
        profileImage = (ImageView)fragmentLayout.findViewById(R.id.profile_image_view);
        profileImage.setImageBitmap(StoryTellerManager.getAccountImage());
        profileName  = (TextView)fragmentLayout.findViewById(R.id.profile_name_text_view);
        profileName.setText(StoryTellerManager.getAccount().getName());
        initSettings();
    }


    //-------------------------------------------------------------------
    // Tokens

    private void initializePlayButtonTEST_MATT(final View tok1,final  View tok2,final View tok3) {
        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int currentTok = StoryTellerManager.getAccount().getTokens();
                if(currentTok > 0)
                {
                    int newTok = currentTok-1;
                    StoryTellerManager.getAccount().setTokens(newTok);
                    refreshTokenUI(newTok, tok1,tok2,tok3,false);
                }
                else
                {
                    Toast toast = Toast.makeText(getContext(),getString(R.string.home_unsufficient_tokens), Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void refreshTokenUI(int nbTokens, View token1, View token2, View token3,boolean setVisible)
    {
        final int TOTAL_HP = 3;
        ToggleButton tb;
        int amountToRemove = TOTAL_HP - nbTokens;

        if(!setVisible) {
            for(int i = 0 ; i < amountToRemove; i++)
            {
                switch (i)
                {
                    case  0:
                        tb = (ToggleButton) token1;
                        tb.setChecked(false);
                        break;
                    case 1:
                        tb  = (ToggleButton) token2;
                        tb.setChecked(false);
                        break;
                    case 2:
                        tb  = (ToggleButton) token3;
                        tb.setChecked(false);
                        break;
                }
            }
        }

        if(setVisible)
        {
            for (int i = 0; i < nbTokens; i++)
            {
                switch (i)
                {
                    case 0:
                        tb  = (ToggleButton) token3;
                        tb.setChecked(true);
                        break;
                    case 1:
                        tb  = (ToggleButton) token2;
                        tb.setChecked(true);
                        break;
                    case 2:
                        tb  = (ToggleButton) token1;
                        tb.setChecked(true);
                        break;
                }
            }
        }
    }


    private void initializeTokens(View token1, View token2, View token3)
    {
        int tok = StoryTellerManager.getAccount().getTokens();
        refreshTokenUI(tok,token1, token2, token3,true);
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
