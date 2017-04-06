package app.storyteller.fragments;

import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.ToggleButton;

import app.storyteller.R;
import app.storyteller.StoryChooserActivity;
import app.storyteller.database.DBHandler;
import app.storyteller.fragments.dialogs.Settings;
import app.storyteller.manager.StoryTellerManager;

/**
 * Created by Mihai on 2017-01-20.
 */
public class MainHomeFragment extends Fragment /*implements View.OnClickListener*/ {

    /**
     * Settings button.
     */
    private ImageButton settings;

    /**
     *
     */
    private ImageView playBtn;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup home = (ViewGroup) inflater.inflate(R.layout.fragment_home, container, false);

        initPlayBtn(home.findViewById(R.id.playBtn));
        initializeSettings(home.findViewById(R.id.settings_btn));
        initializeWebView(home.findViewById(R.id.webview_logo));
        initializeTokens(home.findViewById(R.id.token1), home.findViewById(R.id.token2),home.findViewById(R.id.token3));
        //initializePlayButtonTEST_MATT(home.findViewById(R.id.token1), home.findViewById(R.id.token2),home.findViewById(R.id.token3));
        return home;
    }

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
                    Toast toast = Toast.makeText(getContext(),"You have unsufficient tokens, come back in a few minutes", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }


    // http://stackoverflow.com/questions/2471935/how-to-load-an-imageview-by-url-in-android
    // http://stackoverflow.com/questions/18953632/how-to-set-image-from-url-for-imageview
    private void initializeWebView(View view)
    {
        WebView webview = (WebView) view;
        String url = getGoogleProfileImgURL();
        String name = getGoogleProfileName();
        webview.loadDataWithBaseURL("file:///android_res/drawable/", "<html><body style='background-color:#1aa19b'><img src='"+url+"' style='border-radius:50%;width:50%;max-width:50%;max-height:60%;margin-left:25%;margin-right:25%;margin-bottom:0;padding-bottom:0;' /><p style='text-align:center;color:white;font-size:26px;margin-top:0;padding-top:0;margin-bottom:0;padding-bottom:0;'>"+name+"</p></body></html>", "text/html", "utf-8", null);
    }

    private void initializeSettings(View view) {
        settings = (ImageButton) view;
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
        int tok = getGoogleProfileTokens();
        refreshTokenUI(tok,token1, token2, token3,true);
    }

    private String getGoogleProfileImgURL() {
        DBHandler.openConnection(getContext());
        String str = StoryTellerManager.getAccount().getImageURL();
        DBHandler.closeConnection();
        return str;
    }

    private String getGoogleProfileName() {
        DBHandler.openConnection(getContext());
        String str = StoryTellerManager.getAccount().getName();
        DBHandler.closeConnection();
        return str;
    }
    private int getGoogleProfileTokens() {
        DBHandler.openConnection(getContext());
        int tok = StoryTellerManager.getAccount().getTokens();
        DBHandler.closeConnection();
        return tok;
    }

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_button:
                Intent intent = new Intent( getContext(), SignInActivity.class);
                startActivity(intent);
                break;
    }}*/



    //-----------------------------------------------------------------------------------
    // Mihai's code

    private void initPlayBtn(View v){
        playBtn = (ImageView) v;
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
