package app.storyteller;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;

/**
 * This Activity is a "Transition Activity". Meaning that, when the
 * application is waiting for some events (e.g. Api response, Google
 * authentication process), this Activity is displayed.
 *
 * When the Application is ready, this Activity will proceed to
 * next Activity.
 */
public class LoadingActivity extends AppCompatActivity {

    /**
     *
     */
    public static class ActivityList {
        public static final String SIGN_IN_ACTIVITY = "sign_in_activity";
        public static final String MAIN_ACTIVITY    = "main_activity";
        public static final String STORY_CHOOSER_ACTIVITY = "story_chooser_activity";
    }


    //----------------------------------------------------------------------------
    // Constructor


    /**
     * When the task is done, this is the next Activity that needs
     * to be launched.
     */
    private String nextActivity;

    /**
     * TODO.
     **/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_loading);

        // -- Get the next Activity.
        nextActivity = getIntent().getStringExtra("next_activity");

        // -- Get parameters and do operations in background.
        init();
    }



    //----------------------------------------------------------------------------
    // Methods

    /**
     *
     */
    private void init(){

        // --
        Intent i = getIntent();

        /*
         *
         */
        switch (nextActivity){
            case ActivityList.SIGN_IN_ACTIVITY:
                break;
            case ActivityList.STORY_CHOOSER_ACTIVITY:
                break;
            case ActivityList.MAIN_ACTIVITY:
            default:
                Api.executeRequest(
                        ApiRequests.createProfile(
                            i.getStringExtra("account_id"),
                            i.getStringExtra("account_name"),
                            i.getStringExtra("account_image_url")),
                        this
                );
                break;
        }
    }

    /**
     *
     */
    @Override
    protected void onDestroy() {
        super.onDestroy();
        // --
        Intent intent;

        // --
        switch (nextActivity){
            case ActivityList.SIGN_IN_ACTIVITY:
                intent = new Intent(this, SignInActivity.class);
                break;
            case ActivityList.MAIN_ACTIVITY:
            default:
                intent = new Intent(this, MainActivity.class);
                break;
        }

        // -- Start Activity.
        startActivity(intent);
    }
}
