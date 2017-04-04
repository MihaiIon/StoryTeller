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
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.regex.Pattern;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.constants.RegexPatterns;
import app.storyteller.models.StoryDetails;

/**
 * Created by Mihai on 2017-03-29.
 */

public class StoryEditorActivity extends AppCompatActivity {

    /**
     * The words limits for each sentences.
     */
    private final int MAX_WORDS_ALLOWED = 20;
    private final int MIN_WORDS_ALLOWED = 8;
    private final int MAX_CHRACTERS_ALLOWED = 100;

    /**
     * Information related to the Current Story.
     */
    private String title;
    private String characterName;
    private String theme;
    private String content;

    /**
     * This is value is set to TRUE if the current story is a new
     * Story created by the current User.
     */
    private boolean isNewStory;

    /**
     * The current story ID.
     */
    private int story_id;

    /**
     * By editing this current Story, will it be completed?
     */
    private boolean isCompleted;

    /**
     * The content of the sentence is written here.
     */
    private EditText sentenceInput;

    /**
     * Submits the Story the API.
     */
    private Button submitBtn;

    /**
     *
     */
    private LinearLayout loadingScreen;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_editor);

        // -- Get info related to story.
        getInfoFromExtras();

        // -- Initialise...
        initHeader();
        initThemeInfo();
        initSentenceInput();
        initSubmitBtn();
        initLoadingScreen();
    }


    //-------------------------------------------------------------------
    // Init.

    /**
     *
     */
    private void getInfoFromExtras(){
        // -- Info related to Story.
        title = getIntent().getStringExtra("title");
        characterName = getIntent().getStringExtra("character_name");
        theme = getIntent().getStringExtra("theme");

        // -- Information on the nature of the Story : NEW or ALREADY STARTED.
        isNewStory = getIntent().getBooleanExtra("new_story", false);

        if (isNewStory)
            story_id = getIntent().getIntExtra("story_id", -1);

        // -- By default. TODO.
        isCompleted = false;
    }

    /**
     *
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
        // -- Set Activity Title.
        ((TextView)findViewById(R.id.app_header_title))
                .setText(title);
    }

    /**
     *
     */
    private void initThemeInfo(){
        // -- Set Activity Title.
        ((TextView)findViewById(R.id.story_editor_theme_info))
                .setText(theme);
    }

    /**
     * TODO.
     */
    private void initSentenceInput(){
        sentenceInput = (EditText)findViewById(R.id.story_editor_sentence_input);
        sentenceInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                validate();
            }
        });
    }

    /**
     *
     */
    private void initSubmitBtn(){
        submitBtn = (Button)findViewById(R.id.story_editor_submit_btn);
        submitBtn.setEnabled(true);
        submitBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // -- Enable Loading.
                findViewById(R.id.full_loading_screen).setVisibility(View.VISIBLE);

                // -- Launch API request.
                if (isNewStory){
                    Api.executeRequest(
                        ApiRequests.createStory(
                            new StoryDetails(title, theme, characterName),
                            sentenceInput.getText().toString()),
                            StoryEditorActivity.this);
                } else {
                    Api.executeRequest(
                        ApiRequests.updateStory(
                            story_id, content, isCompleted),
                            StoryEditorActivity.this);
                }

            }
        });
    }


    //-------------------------------------------------------------------
    // Loading Screen methods.

    private void initLoadingScreen(){
        loadingScreen = (LinearLayout)findViewById(R.id.full_loading_screen);
        loadingScreen.setVisibility(View.GONE);
    }



    //-------------------------------------------------------------------
    // Validation

    /**
     *
     */
    private void validate(){
        // -- Get inputs and remove extra spaces.
        String sentence = sentenceInput.getText().toString().trim().replaceAll("\\s+"," ");

        // -- Regex pattern validating the inputs.
        Pattern p = Pattern.compile(RegexPatterns.BASIC_INPUT_VALIDATION);

        /// -- If all fields are well filled, enable the nextBtn.
        if (p.matcher(sentence).matches())
            enableSubmitBtn();

            // -- Else, disable the button.
        else disableSubmitBtn();
    }

    /**
     * Enabling the submitBtn will allow the user to create/update the
     * story on the API.
     */
    private void enableSubmitBtn(){
        submitBtn.setEnabled(true);
        submitBtn.setBackground(ContextCompat.getDrawable(
                getApplicationContext(), R.color.primary));

        submitBtn.setTextColor(ContextCompat.getColor(
                getApplicationContext(), R.color.white));
    }

    /**
     * See enableSubmitBtn().
     */
    private void disableSubmitBtn(){
        submitBtn.setEnabled(false);
        submitBtn.setBackground(ContextCompat.getDrawable(
                getApplicationContext(), R.color.grey));

        submitBtn.setTextColor(ContextCompat.getColor(
                getApplicationContext(), R.color.darkGrey));
    }
}
