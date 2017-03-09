package app.storyteller.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import app.storyteller.MainActivity;
import app.storyteller.R;
import app.storyteller.fragments.dialogs.Settings;
import app.storyteller.sign_in_out;

/**
 * Created by Mihai on 2017-01-20.
 */
public class HomeFragment extends Fragment /*mplements View.OnClickListener*/ {

    /**
     *
     */
    private ImageButton settings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup home = (ViewGroup) inflater.inflate(R.layout.main_home, container, false);

        //
        initializeSettings(home.findViewById(R.id.settings_btn));

        return home;
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
                Intent intent = new Intent( getContext(), sign_in_out.class);
                startActivity(intent);
                break;
    }}*/
}
