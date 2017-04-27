package app.storyteller;

import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageButton;
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
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-03-25.
 */

public class StoryChooserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener, AdapterView.OnItemSelectedListener{

    /**
     * Used to display editable stories
     */
    private ListView listview;
    private ArrayList<Story> stories;

    /**
     * Used to hold the themes for sorting
     */
    private Spinner spinner;
    private String currentTheme;

    /**
     *  Pull-down to refresh
     */
    private SwipeRefreshLayout swipeContainer;

    /**
     *
     */
    private Story selectedStory;

    /**
     *
     */
    private LinearLayout loadingScreen;
    private boolean isActivityLocked;
    private TextView emptyMessage;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_chooser);
        listview = (ListView) findViewById(R.id.story_chooser_story_list);
        emptyMessage = (TextView) findViewById(R.id.emptyMessage);
        emptyMessage.setText(R.string.empty_chooser_story);
        ShowList();
        initAddStoryBtn(findViewById(R.id.story_chooser_add_btn));
        initHeader();
        initLoadingScreen();
        initSwipeContainer();
        initSpinner();
        currentTheme = "All";
        // -- On create, fetch all incomplete stories and display them.
        //fetchIncompleteStories();
    }

    /**
     * When the user comes back from the storyContent editor activity
     * the storyContent he last modified does is not displayed.
     */
    @Override
    protected void onResume() {
        super.onResume();
        AppManager.getTokenManager().startTokensWatcher(this);
        fetchIncompleteStories();
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppManager.getTokenManager().stopTokensWatcher();
    }



    //----------------------------------------------------------------------------
    // Tokens

    /**
     *
     */
    private void initHeader(){
        ((TextView)findViewById(R.id.header_title)).setText(R.string.story_chooser_header_title);
        initBackArrow();
        initTokens();
    }

    /**
     * TODO.
     */
    private void initTokens(){

    }


    /**
     * Initialize the button that allows the user to create new Stories.
     * -- At least one token is required to start a new Story.
     */
    private void initAddStoryBtn(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(AppManager.getTokenManager().getTokens() > 0) {
                    startActivity(new Intent(getApplicationContext(),
                            StoryCreatorActivity.class));
                } else {
                    Toast.makeText(getApplicationContext(),
                            "You have unsufficient tokens",
                            Toast.LENGTH_LONG).show();
                }}});

        pulseAnimation((ImageButton)v);
    }

    /**
     *
     */
    ObjectAnimator objAnim;
    private void pulseAnimation(ImageButton btn){
        objAnim= ObjectAnimator.ofPropertyValuesHolder(btn, PropertyValuesHolder.ofFloat("scaleX", 1.05f), PropertyValuesHolder.ofFloat("scaleY", 1.05f));
        objAnim.setDuration(600);
        objAnim.setRepeatCount(ObjectAnimator.INFINITE);
        objAnim.setRepeatMode(ObjectAnimator.REVERSE);
        objAnim.start();
    }


    //----------------------------------------------------------------------------


    /**
     * Initialize the Pull Down for refresh container
     */
    private void initSwipeContainer(){
        this.swipeContainer = (SwipeRefreshLayout) findViewById(R.id.story_chooser_swipeContainer);
        this.swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                fetchIncompleteStories();
                swipeContainer.setRefreshing(false);

            }
        });
    }

    /**
     * Initialize the Spinner used for sorting
     */
    private void initSpinner(){
        spinner = (Spinner) findViewById(R.id.chooser_spinner);
        spinner.setOnItemSelectedListener(this);
    }

    /**
     * On item change in Spinner, refresh the storyContent list
     * @param parent
     * @param view
     * @param position
     * @param id
     */
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
     * When stories are pulled from the API, parse them using the selected theme.
     */
    public void refreshStoriesList(ArrayList<Story> list) {
        setLockActivity(false);
        System.out.println("************" + isActivityLocked);
        // -- TODO :  Remove loading and place stories in ListView.
        stories = list;
        if (stories.size() > 0) {
            // stories are parsed depending on the current theme
            if (currentTheme.equals(getApplicationContext().getString(R.string.story_chooser_all)))
                stories = list;
            else {
                ArrayList<Story> parsedList = new ArrayList<Story>();
                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).getDetails().getTheme().equals(currentTheme)) {
                        parsedList.add(list.get(i));
                    }
                }
                this.stories = parsedList;
            }

            //reset listview
            ShowList();
            StoryChooserAdapter adapter = new StoryChooserAdapter(this, stories);
            listview.setOnItemClickListener(StoryChooserActivity.this);
            listview.setAdapter(adapter);
        }
        else {
            HideList();
        }


    }
    private void ShowList()
    {
        emptyMessage.setVisibility(View.INVISIBLE);
        listview.setVisibility(View.VISIBLE);
    }
    private void HideList()
    {
        emptyMessage.setVisibility(View.VISIBLE);
        listview.setVisibility(View.INVISIBLE);
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
            intent.putExtra("lastsentence",selectedStory.getSentences().get(selectedStory.getSentences().size()-1).getContent());
            intent.putExtra("character_name",selectedStory.getDetails().getMainCharacter());
            intent.putExtra("theme",selectedStory.getDetails().getTheme());
            intent.putExtra("new_story",false);
            startActivity(intent);
        } else{
            Toast.makeText(
                    getApplicationContext(),
                    "Sorry this storyContent is not available at the moment.",
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
}
