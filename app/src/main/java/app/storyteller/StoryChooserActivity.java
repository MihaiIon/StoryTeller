package app.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ListView;

import java.util.ArrayList;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-03-25.
 */

public class StoryChooserActivity extends AppCompatActivity {

    /**
     *
     */
    private ListView listView;

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

        /* TEST - ZONE - TEST - ZONE - TEST - ZONE */
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
     *
     */
    private void fetchIncompleteStories(){
        Api.executeRequest(ApiRequests.getIncompleteStories(),this);
        showLoadingScreen();
    }

    /**
     *
     */
    public void refreshStoriesList(ArrayList<Story> list){
        hideLoadingScreen();
        // -- TODO :  Remove loading and place stories in ListView.
    }



    //-------------------------------------------------------------------
    // Loading Screen methods.

    private void initLoadingScreen(){
        loadingScreen = (LinearLayout)findViewById(R.id.full_loading_screen);
        hideLoadingScreen();
    }

    private void hideLoadingScreen(){
        loadingScreen.setVisibility(View.GONE);
    }

    private void showLoadingScreen(){
        loadingScreen.setVisibility(View.VISIBLE);
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
