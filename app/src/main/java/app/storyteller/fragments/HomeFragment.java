package app.storyteller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageButton;

import app.storyteller.R;
import app.storyteller.fragments.dialogs.Settings;

/**
 * Created by Mihai on 2017-01-20.
 */
public class HomeFragment extends Fragment /*implements View.OnClickListener*/ {

    /**
     *
     */

    private ImageButton settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup home = (ViewGroup) inflater.inflate(R.layout.main_home, container, false);

        //
        initializeSettings(home.findViewById(R.id.settings_btn));
        initializeWebView(home.findViewById(R.id.webview_logo));
        initializePlayWebView(home.findViewById(R.id.webview_play));

        return home;
    }
    private void initializePlayWebView(View view)
    {
        WebView webview = (WebView) view;
        webview.loadData("","text/html",null);
    }
    private void initializeWebView(View view)
    {
        WebView webview = (WebView) view;
        webview.loadDataWithBaseURL("file:///android_res/drawable/", "<html><body style='background-color:#1aa19b'><img src='test.jpg' style='border-radius:50%;width:50%;max-width:50%;max-height:75%;margin-left:25%;margin-right:25%' /></body></html>", "text/html", "utf-8", null);
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

    /*@Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.profile_button:
                Intent intent = new Intent( getContext(), SignInActivity.class);
                startActivity(intent);
                break;
    }}*/
}
