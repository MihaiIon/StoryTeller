package app.storyteller;

import android.accounts.AccountManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.database.DBHandler;
import app.storyteller.manager.StoryTellerManager;
import app.storyteller.models.Account;
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-03-25.
 */

public class StoryChooserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    /**
     *
     */
    private ListView lstview;
    private String[] titles;
    private String[] previews;
    private String[] themes;
    private String[] characters;

    /**
     *
     */
    private int selectedItem;

    /**
     *
     */
    private boolean isActivityLocked;

    /**
     * Contains all the incomplete Stories.
     */

    /**
     *
     */
    private LinearLayout loadingScreen;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_chooser);
        initAddStoryBtn(findViewById(R.id.story_chooser_add_btn));
        initBackArrow();
        initLoadingScreen();
        // -- On create, fetch all incomplete stories and display them.
        fetchIncompleteStories();

    }


    /**
     *
     */
    private void initAddStoryBtn(View v){
        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //rajouter le code pour verifier la quantité de vies.
                Account currentAccount = StoryTellerManager.getAccount();
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
        titles = new String[list.size()];
        previews = new String[list.size()];
        themes = new String[list.size()];
        characters = new String[list.size()];
        int t;
        for (int i = 0; i < list.size(); i++) {
            Story story = list.get(i);
            titles[i] = story.getDetails().getTitle();
            t = story.getSentences().size(); //get last sentence
            previews[i] = story.getSentences().get(t - 1).getContent();
            themes[i] = story.getDetails().getTheme();
            characters[i] = story.getDetails().getMainCharacter();
        }
        lstview = (ListView) findViewById(R.id.story_chooser_story_list);
        StoryChooserAdapter adapter = new StoryChooserAdapter(this,titles,previews,themes);
        lstview.setOnItemClickListener(StoryChooserActivity.this);
        lstview.setAdapter(adapter);
    }



    //-------------------------------------------------------------------
    // Item Methods

    /**
     *
     */
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        setLockActivity(true);
        selectedItem = position;
        Api.executeRequest(ApiRequests.isStoryLocked(37), this);
    }

    /**
     *
     */
    public void onItemVerified(boolean isLocked){
        setLockActivity(false);
        if (!isLocked){
            Api.executeRequest(ApiRequests.lockStory(0), this);
            Intent intent = new Intent(getApplicationContext(),StoryEditorActivity.class);
            intent.putExtra("title",this.titles[selectedItem]);
            intent.putExtra("character_name",this.characters[selectedItem]);
            intent.putExtra("theme",this.titles[selectedItem]);
            intent.putExtra("new_story",false);
            startActivity(intent);
        } else{
            // Sorry the item is not available
            // TODO : Remove item from list.
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
        findViewById(R.id.story_chooser_back_lyt)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        backToMain();
                    }
        });
    }

    /**
     * When the Back Button is pressed, return to MainActivity.
     */
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        backToMain();
    }

    /**
     *
     */
    private void backToMain(){
        startActivity(new Intent(this, MainActivity.class));
    }
}
