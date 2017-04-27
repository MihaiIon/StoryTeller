package app.storyteller;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.style.LineHeightSpan;
import android.text.style.RelativeSizeSpan;
import android.text.style.TypefaceSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.CompoundButton;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.ToggleButton;

import app.storyteller.database.DBHandler;
import app.storyteller.fragments.MainAllStoriesFragment;
import app.storyteller.manager.AppManager;

/**
 * Created by Vincent on 2017-04-01.
 */

public class StoryReaderActivity extends AppCompatActivity {

    TextView title, author, storyContent;
    ToggleButton favs;
    TextView header_title;
    private ImageButton smallerText;
    private ImageButton biggerText;
    private int textSize;
    private int id;

    int position;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        title = (TextView) findViewById(R.id.story_title);
        author = (TextView) findViewById(R.id.story_author);
        storyContent = (TextView) findViewById(R.id.story_reader_story_container);
        favs= (ToggleButton) findViewById(R.id.story_fav);
        textSize = 0;

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            title.setText(bundle.getString("Title"));
            author.setText("by : " + bundle.getString("Authors"));
            storyContent.setText(getFormattedStoryContent(bundle.getString("Story")));
            //storyContent.setLineSpacing(20f, 1f);
            favs.setChecked(bundle.getBoolean("Favs"));
            position = bundle.getInt("Position");
            id = bundle.getInt("StoryID");
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
                int playerId = AppManager.getAccount().getId();
                if (isChecked) {
                    MainAllStoriesFragment.setFavs(position, true);
                    DBHandler.openConnection(getApplicationContext());
                    DBHandler.addFavorite(playerId,id);
                    DBHandler.closeConnection();
                }
                else {
                    MainAllStoriesFragment.setFavs(position,false);
                    DBHandler.openConnection(getApplicationContext());
                    DBHandler.removeFavorite(playerId,id);
                    DBHandler.closeConnection();
                }
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
        //smallerText = (ImageButton) findViewById(R.id.button_text_size_small);
        biggerText = (ImageButton) findViewById(R.id.button_text_size_big);

        biggerText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                switch (textSize){
                    case 0:
                        textSize = 1;
                        storyContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        break;
                    case 1:
                        textSize = 2;
                        storyContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
                        break;
                    case 2:
                        textSize = 0;
                        storyContent.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
                }
            }
        });
    }


    //----------------------------------------------------------------------------------
    // Mihai's Lab


    private class CustomTypefaceSpan extends TypefaceSpan {

        private final Typeface newType;

        public CustomTypefaceSpan(String family, Typeface type) {
            super(family);
            newType = type;
        }

        @Override
        public void updateDrawState(TextPaint ds) {
            applyCustomTypeFace(ds, newType);
        }

        @Override
        public void updateMeasureState(TextPaint paint) {
            applyCustomTypeFace(paint, newType);
        }

        private void applyCustomTypeFace(Paint paint, Typeface tf) {
            int oldStyle;
            Typeface old = paint.getTypeface();
            if (old == null) {
                oldStyle = 0;
            } else {
                oldStyle = old.getStyle();
            }

            int fake = oldStyle & ~tf.getStyle();
            if ((fake & Typeface.BOLD) != 0) {
                paint.setFakeBoldText(true);
            }

            if ((fake & Typeface.ITALIC) != 0) {
                paint.setTextSkewX(-0.25f);
            }
            paint.setTypeface(tf);
        }
    }

    /**
     *
     */
    private Spannable getFormattedStoryContent(String raw){

        Spannable s = new SpannableString(raw);
        s.setSpan(new RelativeSizeSpan(4f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new LineHeightSpan() {
            @Override
            public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
                fm.bottom = 0;
                fm.descent = 80;
                fm.top = -200;
                fm.ascent =0;
            }
        }, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        s.setSpan(new CustomTypefaceSpan("kaushan", Typeface.createFromAsset(getAssets(), "fonts/kaushan.otf")),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        return s;
    }
}
