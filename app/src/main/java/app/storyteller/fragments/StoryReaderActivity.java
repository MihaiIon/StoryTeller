package app.storyteller.fragments;

import android.os.Bundle;

import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;
import android.widget.ToggleButton;

import app.storyteller.R;

/**
 * Created by Vincent on 2017-04-01.
 */

public class StoryReaderActivity extends AppCompatActivity {

    TextView title, author,story;
    ToggleButton favs;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        title = (TextView) findViewById(R.id.story_title);
        author = (TextView) findViewById(R.id.story_author);
        story= (TextView) findViewById(R.id.story);
        favs= (ToggleButton) findViewById(R.id.story_fav);
        TextView header_title = (TextView)findViewById(R.id.app_header_title);
        header_title.setText("");
        findViewById(R.id.story_chooser_back_lyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            title.setText(bundle.getString("Title"));
            author.setText("by : " + bundle.getString("Authors"));
            story.setText(bundle.getString("Story"));

            favs.setChecked(bundle.getBoolean("Favs"));

        }



    }
}
