package app.storyteller.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.PaintDrawable;
import android.os.AsyncTask;
import android.os.Bundle;


import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import android.widget.TextView;

import android.widget.Toast;
import android.widget.ToggleButton;


import java.sql.Timestamp;

import java.util.Timer;
import java.util.TimerTask;



import app.storyteller.R;
import app.storyteller.StoryChooserActivity;
import app.storyteller.api.ApiRequests;
import app.storyteller.database.DBHandler;
import app.storyteller.fragments.dialogs.Settings;
import app.storyteller.manager.StoryTellerManager;
import app.storyteller.models.Account;


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
    private TextView timerText;
    private ToggleButton token1,token2,token3;
    private int count;
    public int getCount() {
        return count;
    }
    public void setCount(int count) {
        this.count = count;
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        fragmentLayout = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);
        timerText = (TextView) fragmentLayout.findViewById(R.id.timerText);
        token1 = (ToggleButton) fragmentLayout.findViewById(R.id.token1);
        token2 = (ToggleButton) fragmentLayout.findViewById(R.id.token2);
        token3 = (ToggleButton) fragmentLayout.findViewById(R.id.token3);

        initHeader();
        initPlayBtn();
        initTokenUIandThread();

        return fragmentLayout;
    }
// initializeTokens(home.findViewById(R.id.token1), home.findViewById(R.id.token2),home.findViewById(R.id.token3));

    private void initTokenUIandThread() {
        refreshTokenUI(StoryTellerManager.getAccount().getTokens(),token1, token2, token3, true);
        TimerToken timer = new TimerToken();
        timer.execute();
    }


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

    public class TimerToken extends AsyncTask {

        @Override
        protected Object doInBackground(Object[] params) {
            new Reminder(1);
            return null;
        }

        @Override
        protected void onProgressUpdate(Object[] values){
            Account currAcc = StoryTellerManager.getAccount();

            long time = System.currentTimeMillis();
            long diff = time - (long)values[0];
            long diffInSeconds = diff/1000;
            long diffInMinutes = diffInSeconds/60;
            String timeDisplayed = "More in: "+String.valueOf(60 - diffInSeconds);
            timerText.setText(timeDisplayed);

            if(diffInMinutes >= 1)
            {
                int nbTok = currAcc.getTokens();

                if(nbTok < 3)
                {
                    currAcc.setTokens(nbTok+1);
                    refreshTokenUI(currAcc.getTokens(),token1,token2,token3,true);
                    ApiRequests.updateProfile();
                }
            }

        }
        public class Reminder {
            Timer timer;

            public Reminder(int seconds) {
                timer = new Timer();
                timer.scheduleAtFixedRate(new RemindTask(), 0,seconds * 1000);
            }

            class RemindTask extends TimerTask {
                public void run()
                {
                    Timestamp time = StoryTellerManager.getAccount().getLastConnected();
                    publishProgress(time.getTime());
                }
            }
        }
    }
}
