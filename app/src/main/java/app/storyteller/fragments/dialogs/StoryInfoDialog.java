package app.storyteller.fragments.dialogs;

import android.app.DialogFragment;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;
import android.widget.TextView;

import app.storyteller.R;

/**
 * Created by Jeffery on 2017-04-26.
 */

public class StoryInfoDialog extends DialogFragment {

    private View layout;

    public static StoryInfoDialog newInstance(String title, String character, String theme) {
        StoryInfoDialog infoBox = new StoryInfoDialog();
        Bundle args = new Bundle();
        args.putString("title", title);
        args.putString("character_name", character);
        args.putString("theme", theme);
        infoBox.setArguments(args);
        return infoBox;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        layout = inflater.inflate(R.layout.dialog_story_info, container, false);
        initTextViews();
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


    //-------------------------------------------------------------------------------------------
    // Methods

    public void initTextViews()
    {
        ((TextView) layout.findViewById(R.id.dialog_story_info_title))
                .setText(getArguments().getString("title"));

        ((TextView) layout.findViewById(R.id.dialog_story_info_character_name))
                .setText(getArguments().getString("character_name"));

        ((TextView) layout.findViewById(R.id.dialog_story_info_theme))
                .setText(getArguments().getString("theme"));
    }
}
