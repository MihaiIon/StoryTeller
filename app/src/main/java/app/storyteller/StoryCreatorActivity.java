package app.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;

/**
 * Created by Mihai on 2017-03-26.
 */

public class StoryCreatorActivity extends AppCompatActivity {

    /**
     *
     */
    private Button nextBtn;
    private EditText title;
    private EditText character;
    private Spinner theme;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creator);
        initArrow();
        initTitleInput();
        initCharacterInput();
        initThemeSelect();
        initNextBtn();
    }


    //-------------------------------------------------------------------
    // Init.


    /**
     * Triggers back button (returns back to StoryChooserActivity).
     */
    private void initArrow(){
        LinearLayout header = (LinearLayout) findViewById(R.id.story_chooser_back_lyt);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
         });
    }

    /**
     *
     */
    private void initTitleInput(){
        title = (EditText)findViewById(R.id.story_creator_title_input);
        title.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    validate();
                }
            }
        });
    }

    /**
     *
     */
    private void initCharacterInput(){
        character = (EditText)findViewById(R.id.story_creator_title_input);
        character.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus){
                    validate();
                }
            }
        });
    }

    /**
     * TODO. Something intelligent.
     */
    private void initThemeSelect(){
        theme = (Spinner)findViewById(R.id.story_creator_theme_select);
    }

    /**
     * Initialise the R.id.story_creator_next_btn Button, but disable it.
     * Enable it only when all the inputs will be filled, see -> validate().
     */
    private void initNextBtn(){
        nextBtn = (Button)findViewById(R.id.story_creator_next_btn);
        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(v.getContext(), StoryEditorActivity.class));
                finish();
            }
        });
        nextBtn.setEnabled(false);
    }


    //-------------------------------------------------------------------
    // Methods

    /**
     * Checks if all input are filled. If the this is the case, enable
     * the R.id.story_creator_next_btn Button.
     */
    private void validate(){
        if (title.getText().length() != 0 && character.getText().length() != 0)
        {
            nextBtn.setEnabled(true);
            nextBtn.setBackground(
                    ContextCompat.getDrawable(
                            getApplicationContext(),
                            R.drawable.green_button)
            );
            nextBtn.setTextColor(
                    ContextCompat.getColor(
                            getApplicationContext(),
                            R.color.white
                    )
            );
        }
    }
}
