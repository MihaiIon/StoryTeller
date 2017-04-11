package app.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import app.storyteller.adapters.StoryChooserAdapter;
import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.manager.AppManager;
import app.storyteller.models.Account;
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-03-25.
 */

public class StoryChooserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{

    /**
     *
     */
    private ListView listview;
    private Spinner spinner;
    private ArrayList<Story> stories;
    private String currentTheme;

    /**
     *
     */
    private Story selectedStory;

    /**
     *
     */
    private LinearLayout loadingScreen;
    private boolean isActivityLocked;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_chooser);
        initAddStoryBtn(findViewById(R.id.story_chooser_add_btn));
        initHeader();
        initLoadingScreen();
        initSpinner();
        currentTheme = "caca";
        // -- On create, fetch all incomplete stories and display them.
        //fetchIncompleteStories();

    }

    /**
     *
     */
    private void initHeader(){
        initBackArrow();
        ((TextView)findViewById(R.id.header_title))
                .setText(R.string.story_chooser_header_title);
    }

    /**
     *
     */
    private void initAddStoryBtn(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rajouter le code pour verifier la quantité de vies.
                Account currentAccount = AppManager.getAccount();
                int currTok = currentAccount.getTokens();
                if(currTok > 0)
                {
                    startActivity(new Intent(getApplicationContext(), StoryCreatorActivity.class));
                }
                else
                {
                    String amountTime = "10";
                    //check last time a token was given (timestamp) if this current timestamp - lastTokenGiven % 15(mins) == 1 give one life, ==2 two lives, == 3 give all life back
                    Toast toast = Toast.makeText(getApplicationContext(),"You have unsufficient tokens, come back in "+amountTime+" minutes", Toast.LENGTH_LONG);
                    toast.show();
                }
            }
        });
    }

    private void initSpinner(){
        spinner = (Spinner) findViewById(R.id.chooser_spinner);
        spinner.setOnItemSelectedListener(this);

    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        this.currentTheme = parent.getItemAtPosition(position).toString();
        fetchIncompleteStories();
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    //--------------------------------------------------------------------
    // Methods

    /**
     * Fetches all incomplete stories from API that was not last updated
     * by the current user.
     */
    private void fetchIncompleteStories(){
        Api.executeRequest(ApiRequests.getIncompleteStories(),this);
        setLockActivity(true);
    }

    /**
     *
     */
    public void refreshStoriesList(ArrayList<Story> list){
        setLockActivity(false);
        System.out.println("************"+isActivityLocked);
        // -- TODO :  Remove loading and place stories in ListView.
        stories = list;
        listview = (ListView) findViewById(R.id.story_chooser_story_list);
        StoryChooserAdapter adapter = new StoryChooserAdapter(this, stories, currentTheme);
        listview.setOnItemClickListener(StoryChooserActivity.this);
        listview.setAdapter(adapter);
    }



    //-------------------------------------------------------------------
    // Item Methods

    /**
     *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setLockActivity(true);
        selectedStory = stories.get(position);
        Api.executeRequest(ApiRequests.isStoryLocked(selectedStory.getId()), this);
    }

    /**
     *
     */
    public void onItemVerified(boolean isLocked){
        setLockActivity(false);
        if (!isLocked){
            Api.executeRequest(ApiRequests.lockStory(selectedStory.getId()), this);
            Intent intent = new Intent(getApplicationContext(),StoryEditorActivity.class);
            intent.putExtra("id",selectedStory.getId());
            intent.putExtra("title",selectedStory.getDetails().getTitle());
            intent.putExtra("character_name",selectedStory.getDetails().getMainCharacter());
            intent.putExtra("theme",selectedStory.getDetails().getTheme());
            intent.putExtra("new_story",false);
            startActivity(intent);
        } else{
            Toast.makeText(
                    getApplicationContext(),
                    "Sorry this story is not available at the moment.",
                    Toast.LENGTH_SHORT).show();
            // TODO : Do refresh HERE.
            fetchIncompleteStories();
        }
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

    /**
     *
     */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if(isActivityLocked){
            return super.onTouchEvent(event);
        }
        return false;
    }



    //--------------------------------------------------------------------
    // Back Actions.

    /**
     * When the Layout of the Back Arrow is pressed, return to MainActivity.
     */
    private void initBackArrow(){
        findViewById(R.id.header_back_arrow)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        onBackPressed();
                    }
        });
    }

    /**
     * When the Back Button is pressed, return to MainActivity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }

    /**
     * When the user comes back from the story editor activity
     * the story he last modified does is not displayed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        fetchIncompleteStories();
    }
}
