package app.storyteller;

import android.graphics.Paint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.TextPaint;
import android.text.TextUtils;
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



    private ImageButton smallerText;
    private ImageButton biggerText;
    private int textSize;

    int

    /*
     * View related.
     */
    private TextView storyTitleTextView, storyAuthorTextView, storyContentTextView;
    private ToggleButton starToggleBtn;

    /*
     * Story Related.
     */
    private int storyID, storyPosition;
    private String storyTitle, storyContent, storyCreator, storyCharacter;
    private boolean storyIsFavorite;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_read_story);

        storyTitleTextView = (TextView) findViewById(R.id.story_title);
        storyAuthorTextView = (TextView) findViewById(R.id.story_author);
        storyContentTextView = (TextView) findViewById(R.id.story_reader_story_container);
        starToggleBtn = (ToggleButton) findViewById(R.id.story_fav);
        textSize = 0;

        Bundle bundle = getIntent().getExtras();
        if(bundle!=null){
            // -- Get
            storyID = bundle.getInt("story_id");
            storyTitle = bundle.getString("story_title");
            storyCreator = bundle.getString("story_creator");
            storyContent = bundle.getString("story_content");
            storyCharacter = bundle.getString("story_character");
            storyIsFavorite = bundle.getBoolean("is_fav");
            storyPosition = bundle.getInt("story_position");

            // -- Set
            storyAuthorTextView.setText("by : " + storyCreator);
            storyContentTextView.setText(getFormattedStoryContent(storyContent));
            starToggleBtn.setChecked(storyIsFavorite);
        }

        InitHeader();
        initTextButtons();
        InitToggleStar();
    }

    public void InitToggleStar(){
        starToggleBtn = (ToggleButton) findViewById(R.id.story_fav);
        
        starToggleBtn.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                int playerId = AppManager.getAccount().getId();
                if (isChecked) {
                    MainAllStoriesFragment.setFavs(storyPosition, true);
                    DBHandler.openConnection(getApplicationContext());
                    DBHandler.addFavorite(playerId,storyID);
                    DBHandler.closeConnection();
                }
                else {
                    MainAllStoriesFragment.setFavs(storyPosition,false);
                    DBHandler.openConnection(getApplicationContext());
                    DBHandler.removeFavorite(playerId,storyID);
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
                        storyContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
                        break;
                    case 1:
                        textSize = 2;
                        storyContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 23);
                        break;
                    case 2:
                        textSize = 0;
                        storyContentTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 17);
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

        /*
         * Modify first letter.
         */
        Spannable firstLetter = new SpannableString(raw.substring(0,1)+"  ");
        firstLetter.setSpan(new RelativeSizeSpan(4f), 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        firstLetter.setSpan(new LineHeightSpan() {
            @Override
            public void chooseHeight(CharSequence text, int start, int end, int spanstartv, int v, Paint.FontMetricsInt fm) {
                fm.bottom = 0;
                fm.descent = 100;
                fm.top = -200;
                fm.ascent =0;
            }
        }, 0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        firstLetter.setSpan(new CustomTypefaceSpan("kaushan", Typeface.createFromAsset(getAssets(), "fonts/kaushan.otf")),
                0, 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);

        /*
         *
         */
        /*String txt = raw.substring(1);
        int index = txt.indexOf();
        while (index >= 0) {
            System.out.println(index);
            index = word.indexOf(guess, index + 1);
        }
        Spannable firstLetter2 = new SpannableString(raw.substring(0,1)+"  ");*/

        return new SpannableString(TextUtils.concat(firstLetter, raw.substring(1)));
    }
}
