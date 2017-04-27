package app.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import app.storyteller.constants.RegexPatterns;

/**
 * Created by Mihai on 2017-03-26.
 */
public class StoryCreatorActivity extends AppCompatActivity {

    /**
     *
     */
    private final int MAX_CHARACTERS_FOR_TITLE = 20;
    private final int MAX_CHARACTERS_FOR_CHARACTER = 20;

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
        findViewById(R.id.header_back_arrow)
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
        title.setMaxLines(1);
        title.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                validate();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            /**
             *  Check for illegal character. TODO.
             */
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    char c = s.charAt(s.length()-1);
                    if (!validateChar(c)){
                        Toast.makeText(getApplicationContext(),
                                "\""+c+"\" : " + getString(R.string.story_creator_invalid_char),
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });
    }

    /**
     *
     */
    private void initCharacterInput(){
        character = (EditText)findViewById(R.id.story_creator_character_input);
        title.setMaxLines(1);
        character.addTextChangedListener(new TextWatcher() {

            public void afterTextChanged(Editable s) {
                validate();
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            /**
             *  Check for illegal character. TODO.
             */
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length()!=0){
                    char c = s.charAt(s.length()-1);
                    if (!validateChar(c)){
                        Toast.makeText(getApplicationContext(),
                                "\""+c+"\" : " + getString(R.string.story_creator_invalid_char),
                                Toast.LENGTH_SHORT).show();
                    }
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
        disableNextBtn();
    }


    //-------------------------------------------------------------------
    // Methods

    /**
     * Checks if all input are filled. If the this is the case, enable
     * the R.id.story_creator_next_btn Button.
     */
    private void validate(){

        // -- Get inputs and remove extra spaces.
        String titleText = title.getText().toString().trim().replaceAll("\\s+"," ");
        String characterText = character.getText().toString().trim().replaceAll("\\s+"," ");

        // -- Regex pattern validating the inputs.
        Pattern p = Pattern.compile(RegexPatterns.BASIC_INPUT_VALIDATION);

        /// -- If all fields are well filled, enable the nextBtn.
        if (p.matcher(titleText).matches() && p.matcher(characterText).matches())
            enableNextBtn();

        // -- Else, disable the button.
        else disableNextBtn();
    }

    private boolean validateChar(char c){
        return Pattern.compile(RegexPatterns
                .CHARACTER_VALIDATION).matcher(""+c).matches();
    }


    /**
     * Enabling the nextBtn will allow the user to proceed
     * and write the storyContent first sentence.
     */
    private void enableNextBtn(){
        nextBtn.setEnabled(true);
        nextBtn.setBackground(ContextCompat.getDrawable(
                getApplicationContext(), R.color.primary));

        nextBtn.setTextColor(ContextCompat.getColor(
                getApplicationContext(), R.color.white));
    }

    /**
     * See enableNextBtn().
     */
    private void disableNextBtn(){
        nextBtn.setEnabled(false);
        nextBtn.setBackground(ContextCompat.getDrawable(
                getApplicationContext(), R.color.grey));
        nextBtn.setTextColor(ContextCompat.getColor(
                getApplicationContext(), R.color.darkGrey));
    }
}
