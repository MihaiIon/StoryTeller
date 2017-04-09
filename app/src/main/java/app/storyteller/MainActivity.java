package app.storyteller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import app.storyteller.fragments.MainAllStoriesFragment;
import app.storyteller.fragments.MainHomeFragment;

public class MainActivity extends AppCompatActivity {

    /**
     * See onBackPressed() method.
     */
    private boolean backBtnPressed;

    /**
     * Number of slides contained in the mainActivity. The slides includes : this Home screen,
     * the PartyMode screen and the Story screen.
     */
    private static final int NUM_SLIDES = 2;

    /**
     * Handles animations and allows swiping horizontally to access the available slides.
     */
    private ViewPager slider;

    /**
     * The pager adapter, which provides the pages to the view pager widget.
     */
    private PagerAdapter sliderAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Instantiate a ViewPager and a PagerAdapter.
        slider = (ViewPager) findViewById(R.id.mainActivity);
        sliderAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        slider.setAdapter(sliderAdapter);
        backBtnPressed = false;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                onBackPressed();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    /**
     *
     */
    @Override
    public void onBackPressed() {
        if (slider.getCurrentItem() == 0) {
            if (backBtnPressed){
                finishAffinity();
            } else {
                backBtnPressed = true;
                Toast.makeText(
                        getApplicationContext(),
                        getString(R.string.main_press_back_again_exit),
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            // Otherwise, select the previous step.
            slider.setCurrentItem(0);
        }
    }


    /**
     * A simple pager adapter that represents 3 ScreenSlidePageFragment objects. Depending
     * on the current position in the slider (pager), a different slide is shown.
     */
    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment frag;
            switch (position) {
                case 1:
                    frag =  new MainAllStoriesFragment();
                    break;
                default:
                    frag=  new MainHomeFragment();
                    break;
            }
            return frag;
        }
        @Override
        public int getCount() {
            return NUM_SLIDES;
        }
    }
}
