package app.storyteller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

/**
 * Created by Mihai on 2017-03-29.
 */

public class StoryEditorActivity extends AppCompatActivity {

    /**
     * The words limit for each sentences.
     */
    private final int MAX_WORDS_ALLOWED = 25;

    /**
     * Information related to the Current Story.
     */
    private String title;
    private String characterName;
    private String theme;

    /**
     * This is value is set to TRUE if the current story is a new
     * Story created by the current User.
     */
    private boolean newStory;

    /**
     *
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_editor);

        // -- Get info related to story.
        getInfo();

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
    private void getInfo(){
        // -- Info related to Story.
        title = getIntent().getStringExtra("title");
        characterName = getIntent().getStringExtra("character_name");
        theme = getIntent().getStringExtra("theme");

        // -- Information on the nature of the Story : NEW or ALREADY STARTED.
        newStory = getIntent().getBooleanExtra("new_story", false);
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
     *
     */
    private void initSentenceInput(){

    }

    /**
     *
     */
    private void initSubmitBtn(){
        findViewById(R.id.story_editor_submit_btn)
            .setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

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
