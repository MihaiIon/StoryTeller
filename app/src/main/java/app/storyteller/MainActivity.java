package app.storyteller;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.Toast;

import app.storyteller.fragments.MainAllStoriesFragment;
import app.storyteller.fragments.MainHomeFragment;
import app.storyteller.manager.AppManager;

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

    /**
     *
     * @param savedInstanceState
     */
    private Fragment currentDisplayedFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Instantiate a ViewPager and a PagerAdapter.
        slider = (ViewPager) findViewById(R.id.mainActivity);
        sliderAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        slider.setAdapter(sliderAdapter);
        backBtnPressed = false;

        // --
        if(AppManager.getAccount().getId() == 2){
            Toast.makeText(getApplicationContext(), "Sorry you are banned from the app for spamming poop in the database.", Toast.LENGTH_LONG).show();
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        AppManager.getTokenManager().stopTokensWatcher();
    }

    @Override
    protected void onResume() {
        super.onResume();
        AppManager.getTokenManager().startTokensWatcher(this);
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
        private ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 1:
                   /* MainActivity.updateSlidersIndicators(
                            (MainHomeFragment)currentDisplayedFragment, false);*/
                    currentDisplayedFragment = new MainAllStoriesFragment();
                    break;
                default:
                    currentDisplayedFragment = new MainHomeFragment();
                    /*MainActivity.updateSlidersIndicators(
                            (MainHomeFragment)currentDisplayedFragment, true);*/
                    break;
            }
            return currentDisplayedFragment;
        }
        @Override
        public int getCount() {
            return NUM_SLIDES;
        }
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
     * @param home :
     * @param isHome :
     */
    public static void updateSlidersIndicators(MainHomeFragment home, boolean isHome){
        // -- First Circle.
        home.getActivity().findViewById(R.id.slides_indicator_1)
                .setBackground(ContextCompat.getDrawable(home.getActivity(),
                        (isHome ? R.drawable.slides_indicator_full
                            : R.drawable.slides_indicator_empty)));

        // -- Second Cricle.
        home.getActivity().findViewById(R.id.slides_indicator_1)
                .setBackground(ContextCompat.getDrawable(home.getActivity(),
                        (isHome ? R.drawable.slides_indicator_empty
                                : R.drawable.slides_indicator_full)));
    }
}
