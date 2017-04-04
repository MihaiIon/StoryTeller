package app.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
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
    // Validation

    /**
     *
     */
    private void validate(){

    }
}
