package app.storyteller.fragments.dialogs;

import android.os.Bundle;
import android.provider.*;
import android.provider.Settings;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.storyteller.R;
import app.storyteller.manager.AppManager;

/**
 * Created by Jeffery on 2017-04-26.
 */

public class InformationBox extends DialogFragment {

    private View layout;
    private TextView titleInfo;
    private TextView mainCharInfo;
    private TextView themeInfo;

    public static InformationBox newInstance(String title, String character, String theme) {

        System.out.println("INFOBOXHEREE!!");

        InformationBox infoBox = new InformationBox();
        Bundle args = new Bundle();
        args.putString("titleInfo", title);
        args.putString("characterInfo", character);
        args.putString("themeInfo", theme);
        infoBox.setArguments(args);
        return infoBox;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.dialog_infobox, container, false);

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

    //METHODS

    public void setText()
    {

        titleInfo = (TextView) this.getDialog().findViewById(R.id.dialog_infobox_title);
        mainCharInfo = (TextView) this.getDialog().findViewById(R.id.dialog_infobox_character);
        themeInfo = (TextView) this.getDialog().findViewById(R.id.dialog_infobox_theme);
        
        titleInfo.setText(getArguments().getString("titleInfo"));
        mainCharInfo.setText(getArguments().getString("characterInfo"));
        themeInfo.setText(getArguments().getString("themeInfo"));
    }
}
