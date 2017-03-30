package app.storyteller;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.LinearLayout;

/**
 * Created by Mihai on 2017-03-26.
 */

public class StoryCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_story_creator);
        initArrow();
        //Reste à changer le header a ton gout en code
    }


    private void initArrow(){
        LinearLayout header = (LinearLayout) findViewById(R.id.story_chooser_back_lyt);
        header.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
         });
    }

}
