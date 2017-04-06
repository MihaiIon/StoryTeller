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
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.database.DBHandler;
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-03-25.
 */

public class StoryChooserActivity extends AppCompatActivity implements AdapterView.OnItemClickListener{

    /**
     *
     */
    private ListView lstview;
    private ArrayList<Story> stories;

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
                startActivity(new Intent(getApplicationContext(), StoryCreatorActivity.class));

                //MATT TON CODE VAS ICI POUR ENLEVER LES PLUMES


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
        stories = list;
        lstview = (ListView) findViewById(R.id.story_chooser_story_list);
        StoryChooserAdapter adapter = new StoryChooserAdapter(this, stories);
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
            intent.putExtra("title",this.stories.get(selectedItem).getDetails().getTitle());
            intent.putExtra("character_name",this.stories.get(selectedItem).getDetails().getMainCharacter());
            intent.putExtra("theme",this.stories.get(selectedItem).getDetails().getTheme());
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
