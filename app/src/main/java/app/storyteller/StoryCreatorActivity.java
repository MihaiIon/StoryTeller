package app.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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
        initHeader();
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
    private void initHeader(){
        // -- Set Arrow Button.
        findViewById(R.id.app_header_arrow_btn)
            .setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
         });

        // -- Set title
        ((TextView)findViewById(R.id.app_header_title))
                .setText(getString(R.string.story_creator_header_title));
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

                /**
                 * Start new Activity and send infos.
                 */
                Intent i = new Intent(v.getContext(), StoryEditorActivity.class);
                i.putExtra("title", title.getText().toString());
                i.putExtra("theme", theme.getSelectedItem().toString());
                i.putExtra("character_name", character.getText().toString());
                i.putExtra("new_story", true);

                startActivity(i);
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

        boolean validation = true;

        String titleText = title.getText().toString();
        String characterText = character.getText().toString();

        //validate for double char that shouldn't be doubled
        //better way?
        for (int i = 0; i < titleText.length() - 1; i++) {
            if(titleText.charAt(i) == ' ' && titleText.charAt(i+1) == ' ')
                validation = false;
            if(titleText.charAt(i) == ',' && titleText.charAt(i+1) == ',')
                validation = false;
            if(titleText.charAt(i) == ';' && titleText.charAt(i+1) == ';')
                validation = false;
            if(titleText.charAt(i) == ':' && titleText.charAt(i+1) == ':')
                validation = false;
        }
        for (int i = 0; i < characterText.length() - 1; i++) {
            if(characterText.charAt(i) == ' ' && characterText.charAt(i+1) == ' ')
                validation = false;
            if(characterText.charAt(i) == ',' && characterText.charAt(i+1) == ',')
                validation = false;
            if(characterText.charAt(i) == ';' && characterText.charAt(i+1) == ';')
                validation = false;
            if(characterText.charAt(i) == ':' && characterText.charAt(i+1) == ':')
                validation = false;
        }

        //validate for length
        if (title.getText().length() == 0 || character.getText().length() == 0)
            validation = false;

        if (validation)
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
