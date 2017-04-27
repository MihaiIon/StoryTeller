package app.storyteller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Pattern;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.constants.RegexPatterns;
import app.storyteller.manager.AppManager;
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
    private final int MAX_NUM_SENTENCE_TO_COMPLETE_STORY = 10;

    /**
     * Information related to the Current Story.
     */
    private String title;
    private String characterName;
    private String theme;
    private String content;
    private String lastSentenceString;

    /**
     * This is value is set to TRUE if the current storyContent is a new
     * Story created by the current User.
     */
    private boolean isNewStory;

    /**
     * The current storyContent ID.
     */
    private int story_id;

    /**
     * By editing this current Story, will it be completed?
     */
    private boolean completionEnabled;

    /**
     * The content of the sentence is written here.
     */
    private EditText sentenceInput;

    /**
     * The content of the last sentence written
     */
    private TextView lastSentece;
    /**
     * Submits the Story the API.
     */
    private Button submitBtn;
    private ImageButton infoBtn;

    /**
     *
     */
    private LinearLayout loadingScreen;
    private boolean isActivityLocked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_editor);
        initLoadingScreen();

        // -- Get info related to storyContent.
        getInfoFromExtras();

        // -- Initialise...
        initHeader();
        initThemeInfo();
        initSentenceInput();
        initSubmitBtn();
        initLastSentence();
        initInfoBtn();

        // change le layout pour quand le clavier swipe up
        // getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
    }


    @Override
    protected void onPause() {
        super.onPause();
        Api.executeRequest(ApiRequests.unlockStory(story_id), StoryEditorActivity.this);
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
        story_id = getIntent().getIntExtra("id", -1);
        lastSentenceString = getIntent().getStringExtra("lastsentence");

        // -- Information on the nature of the Story : NEW or ALREADY STARTED.
        isNewStory = getIntent().getBooleanExtra("new_story", false);

        // -- Get numbers of sentences in the current Story (if not new).
        if (!isNewStory) {
            setLockActivity(true);
            Api.executeRequest(ApiRequests.getStoryCompletionState(story_id), this);
        }
    }

    /**
     *
     */
    private void initLastSentence(){
        lastSentece = (TextView) findViewById(R.id.previous_sentence);
        lastSentece.setText(lastSentenceString);
    }

    /**
     *
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
     *  Initializes the information button by
     *  sending appropriate info to the created toast
     */

    private void initInfoBtn()
    {
        infoBtn = (ImageButton) findViewById(R.id.story_editor_info_btn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            //Makes a toast
            @Override
            public void onClick(View v) {
                System.out.println("HEREUCK");
                Toast.makeText(
                        getApplicationContext(),
                        "Main Character: "+ characterName + " || Theme: "+ theme,
                        Toast.LENGTH_LONG).show();
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
                            story_id, sentenceInput.getText().toString(), completionEnabled),
                            StoryEditorActivity.this);
                }

                // -- Unlock Story on API and show loading screen.
                AppManager.getTokenManager().consumeToken(StoryEditorActivity.this);
                setLockActivity(true);
            }
        });
    }


    //-------------------------------------------------------------------
    // Methods

    /**
     *
     * @param storySentenceNb
     */
    public void onStoryCompletionResult(int storySentenceNb){
        setLockActivity(false);
        completionEnabled = storySentenceNb >= MAX_NUM_SENTENCE_TO_COMPLETE_STORY-1;
        if (completionEnabled) {
            Toast.makeText(
                    getApplicationContext(),
                    "This is the end! Complete the Story and end it!!!",
                    Toast.LENGTH_LONG).show();
        }
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
     * storyContent on the API.
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


    //-------------------------------------------------------------------
    // Loading Screen methods.

    private void initLoadingScreen(){
        loadingScreen = (LinearLayout)findViewById(R.id.full_loading_screen);
        loadingScreen.setBackground(ContextCompat
                .getDrawable(getApplicationContext(), R.color.semiTransparent));
        hideLoadingScreen();
    }

    private void hideLoadingScreen(){
        loadingScreen.setVisibility(View.GONE);
    }
    private void showLoadingScreen(){
        loadingScreen.setVisibility(View.VISIBLE);
    }

    /**
     * If TRUE, removes all users interaction with the current activity. Setting
     * it to FALSE will reactivate all the listeners.
     * @param value
     */
    private void setLockActivity(boolean value){
        if (value){
            showLoadingScreen();
            getWindow().setFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE,
                    WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        } else {
            hideLoadingScreen();
            getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE);
        }
        isActivityLocked = value;
    }
}
