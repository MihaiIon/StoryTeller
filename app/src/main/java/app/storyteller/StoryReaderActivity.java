package app.storyteller;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import app.storyteller.fragments.MainAllStoriesFragment;

/**
 * Created by Vincent on 2017-04-01.
 */

public class StoryReaderActivity extends AppCompatActivity {

    TextView title, author,story;
    ToggleButton favs;
    TextView header_title;
    private Button textSizeCycle;
    private int textSize;

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        title = (TextView) findViewById(R.id.story_title);
        author = (TextView) findViewById(R.id.story_author);
        story= (TextView) findViewById(R.id.story);
        favs= (ToggleButton) findViewById(R.id.story_fav);
        textSize = 0;





        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            title.setText(bundle.getString("Title"));
            author.setText("by : " + bundle.getString("Authors"));
            story.setText(bundle.getString("Story"));
            favs.setChecked(bundle.getBoolean("Favs"));
            position = bundle.getInt("Position");
        }

        InitHeader();
        initTextButtons();
        InitToggleStar();

    }

    public void InitToggleStar(){
        favs = (ToggleButton) findViewById(R.id.story_fav);


        favs.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    MainAllStoriesFragment.setFavs(position,true);
                else MainAllStoriesFragment.setFavs(position,false);
            }
        });

    }
    public void InitHeader()
    {
        this.findViewById(R.id.header_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    private void initTextButtons(){
        textSizeCycle = (Button) findViewById(R.id.button_text_size);

        textSizeCycle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (textSize){
                    case 0:
                        textSize = 1;
                        story.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        break;
                    case 1:
                        textSize = 2;
                        story.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
                        break;
                    case 2:
                        textSize = 0;
                        story.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                        break;
                }
            }
        });



    }
}
