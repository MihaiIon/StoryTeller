package app.storyteller.fragments.dialogs;

import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.FrameLayout;

import app.storyteller.R;
import app.storyteller.manager.AppManager;

/**
 * Created by Jeffery on 2017-04-26.
 */

public class InformationBox extends DialogFragment {

    private View layout;

    public static InformationBox newInstance() {
        InformationBox infoBox = new InformationBox();
        Bundle args = new Bundle();
        infoBox.setArguments(args);
        return infoBox;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        layout = inflater.inflate(R.layout.dialog_settings, container, false);

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

}
