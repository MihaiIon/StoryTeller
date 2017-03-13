package app.storyteller;

import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import app.storyteller.fragments.AllStoriesFragment;
import app.storyteller.fragments.HomeFragment;
import app.storyteller.fragments.PartyModeFragment;

public class MainActivity extends FragmentActivity {


    /**
     * Number of slides contained in the mainActivity. The slides includes : this Home screen,
     * the PartyMode screen and the Story screen.
     */
    private static final int NUM_SLIDES = 3;

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

        // Set on the Home screen by default.
        slider.setCurrentItem(1);

        // TESTING
        //Api.init(this);
        //Api.createProfile("allo");
    }

    @Override
    public void onBackPressed() {
        if (slider.getCurrentItem() == 1) {
            // If the user is currently looking at the first step, allow the system to handle the
            // Back button. This calls finish() on this activity and pops the back stack.
            // super.onBackPressed();
        } else {
            // Otherwise, select the previous step.
            slider.setCurrentItem(1);
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
            switch (position) {
                case 2:  return new AllStoriesFragment();
                case 0:  return new PartyModeFragment();
                default: return new HomeFragment();

            }
        }

        @Override
        public int getCount() {
            return NUM_SLIDES;
        }
    }
}
