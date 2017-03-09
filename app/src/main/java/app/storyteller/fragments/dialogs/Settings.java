package app.storyteller.fragments.dialogs;

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

import app.storyteller.R;

/**
 * Created by Mihai on 2017-01-28.
 */
public class Settings extends DialogFragment {

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

        // Here we choose and set the style of our dialog.
        int style = DialogFragment.STYLE_NORMAL, theme = 0;
        setStyle(style, theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.settings, container, false);

        // Add listeners to buttons.
        initializeClearDBBtn(view.findViewById(R.id.settings_clearDB_btn));
        initializeTokensBtn(view.findViewById(R.id.settings_addTokens_btn));
        initializeExitBtn(view.findViewById(R.id.settings_exit_btn));

        return view;
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



    /**
     * Provides a way to
     */
    private int count;

    /**
     * -- DEBUG TOOL --
     *
     * Resets the database on the server. Useful when the app's data structure has been
     * changed or is not in sync with the server's database.
     */
    private void initializeClearDBBtn(View view){
        count = 0;
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (count) {
                    case 0:
                        Toast.makeText(getContext(), "Are you sure?", Toast.LENGTH_SHORT).show();
                        count++;
                        break;
                    case 1:
                        Toast.makeText(getContext(), "DBHandler cleared.", Toast.LENGTH_SHORT).show();
                        // Api.resetDatabase();
                        count++;
                        break;
                    default:
                        Toast.makeText(getContext(), "DBHandler already cleared.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    /**
     * -- DEBUG TOOL --
     *
     *  Adds tokens to the current User.
     */
    private void initializeTokensBtn(View view){
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), "100 Tokens added!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * -- DEBUG TOOL --
     *
     * When the "Exit" button is pressed, the dialog is dismissed.
     */
    private void initializeExitBtn(View view){
        Button btn = (Button) view;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });
    }
}
