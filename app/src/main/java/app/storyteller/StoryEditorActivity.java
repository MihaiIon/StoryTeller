package app.storyteller;

import android.app.Fragment;
import android.app.FragmentTransaction;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spannable;
import android.text.TextWatcher;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.constants.RegexPatterns;
import app.storyteller.fragments.dialogs.StoryInfoDialog;
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


        if (lastSentenceString == null){
            findViewById(R.id.layout_previous_sentence).setVisibility(View.GONE  );
            findViewById(R.id.text_previous_sentence).setVisibility(View.GONE);
        }

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

            InputFilter[] fa = new InputFilter[1];

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                int wordCount = countWords(s.toString());

                System.out.println(wordCount);

                /*if (wordCount < MIN_WORDS_ALLOWED)
                {
                    disableSubmitBtn();
                }*/

                if(wordCount >= MAX_WORDS_ALLOWED+1) {
                    Toast.makeText(
                            getApplicationContext(),
                            "Maximum amount of words reached (" +MAX_WORDS_ALLOWED+ ")",
                            Toast.LENGTH_LONG).show();
                    fa[0] = new InputFilter.LengthFilter(count+1);
                    sentenceInput.setFilters(fa);
                }
                else
                {
                    //46 being the longuest word that is not a chemistry formula
                    fa[0] = new InputFilter.LengthFilter(100);
                    sentenceInput.setFilters(fa);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                verify_mentions(s);
                validate();
            }
        });
    }

    /**
     *  Initializes the information button by
     *  sending appropriate info to the created dialog
     */

    private void initInfoBtn()
    {
        infoBtn = (ImageButton) findViewById(R.id.story_editor_info_btn);
        infoBtn.setOnClickListener(new View.OnClickListener() {
            //Makes a toast
            @Override
            public void onClick(View v) {
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                Fragment prev = getFragmentManager().findFragmentByTag("dialog");
                if (prev != null) { ft.remove(prev); }
                ft.addToBackStack(null);

                // Create and show the dialog.
                StoryInfoDialog s = StoryInfoDialog.newInstance(title, characterName, theme);
                s.show(ft, "title");
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

    /**
     * Counts the amount of words in s
     * Credit: http://stackoverflow.com/questions/5864159/count-words-in-a-string-method
     * @param s
     * @return wordcount
     */

    private int countWords(String s)
    {
        int wordCount = 0;

        System.out.println(s);

        boolean word = false;
        int endOfLine = s.length() - 1;

        for (int i = 0; i < s.length(); i++) {
            // if the char is a letter, word = true.
            if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                word = true;
                // if char isn't a letter and there have been letters before,
                // counter goes up.
            } else if (!Character.isLetter(s.charAt(i)) && word) {
                wordCount++;
                word = false;
                // last word of String; if it doesn't end with a non letter, it
                // wouldn't count without this.
            } else if (Character.isLetter(s.charAt(i)) && i == endOfLine) {
                wordCount++;
            }
        }

        return wordCount;
    }

    private void verify_mentions(Editable text) {
        StyleSpan[] ss = text.getSpans(0,text.length(),StyleSpan.class);

        for(int i=0 ;i<ss.length ; i++){
            text.removeSpan(ss[i]);
        }

        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.black)),
                0, text.length(),
                0);

        //* NEW WAY TO FIND THE CARACTER NAME
        //* TODO : FIX REGEX
        String INPUT = Pattern.quote(characterName);
        String REGEX = "(?:[.-?!]|\\s)" + INPUT + "(?:[.-?!]|\\s)" ;
        Pattern p = Pattern.compile(REGEX);
        Matcher m = p.matcher(text.toString());

        while(m.find()){;
            text.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                    m.start()+1, m.end()-1,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
            text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary)),
                    m.start()+1, m.end()-1,
                    0);
        }

        /////////////////////////////////////////////////////////////////
        /*
        if(text.length() > 0) {
            String s = text.toString();

            int lengthOfCharacterName = countWords(characterName);

            boolean word = false;
            int endOfLine = s.length() - 1;

            int wordStart = 0;
            int wordEnd = 0;

            for (int i = 0; i < s.length(); i++) {
                // if the char is a letter, word = true.
                if (Character.isLetter(s.charAt(i)) && i != endOfLine) {
                    word = true;
                    wordEnd = i;
                }
                // if char isn't a letter and there have been letters before,
                // counter goes up.
                else if (!Character.isLetter(s.charAt(i)) && word || Character.isLetter(s.charAt(i)) && i == endOfLine) {

                    String evaluatedWord = s.substring(wordStart, wordEnd + 1);

                    if (evaluatedWord.equals(characterName) || evaluatedWord.equals(" " + characterName)) {
                        text.setSpan(new android.text.style.StyleSpan(android.graphics.Typeface.BOLD),
                                wordStart, wordEnd + 1,
                                Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        text.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.primary)),
                                wordStart, wordEnd + 1,
                                0);
                    }
                    wordStart = wordEnd + 1;
                    word = false;

                }
            }
        }
        */
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
        {    enableSubmitBtn();
        }

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
