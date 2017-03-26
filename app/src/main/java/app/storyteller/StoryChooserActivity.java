package app.storyteller;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Mihai on 2017-03-25.
 */

public class StoryChooserActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_chooser);
        initBackArrow();
    }


    //--------------------------------------------------------------------
    // Back Actions.

    /**
     * When the Back Arrow is pressed, return to MainActivity.
     */
    private void initBackArrow(){
        ((ImageButton)findViewById(R.id.story_chooser_back_btn))
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
