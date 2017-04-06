package app.storyteller.fragments;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;

/**
 * Created by Andre on 2017-03-13.
 */
//ref: https://www.reddit.com/r/androiddev/comments/1pke7v/fragments_belonging_to_a_viewpager_are_not_being/

public class MainPagerFragment extends Fragment {

    private static final int NUM_PAGES = 1;
    private ViewPager mPager;
    private PagerAdapter mPagerAdapter;

    /**
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_screen_slide, container, false);

        mPager = (ViewPager) rootView.findViewById(R.id.pager);
        mPagerAdapter = new ScreenSlidePagerAdapter(getFragmentManager());
        new setAdapterTask().execute();

        return rootView;
    }
    */

    private class PagerFragmentAdapter extends FragmentStatePagerAdapter {
        public PagerFragmentAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            switch (position) {
                case 0: return new MainHomeFragment();
                case 1: return new MainAllStoriesFragment();
                default: return null;
            }
        }

        @Override
        public int getCount() {
            return NUM_PAGES;
        }
    }

}

