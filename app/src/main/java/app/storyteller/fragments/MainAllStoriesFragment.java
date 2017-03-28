package app.storyteller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import app.storyteller.R;

/**
 * Created by Mihai on 2017-01-20.
 */

public class MainAllStoriesFragment extends Fragment {
    ListView lv;
    String[] items;

    LinearLayout all_stories;
    LinearLayout my_stories;
    LinearLayout favs_stories;
    LinearLayout navigator[];

    /**
     * @Override public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
     * ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_all_stories, container, false);
     * <p>
     * return rootView;
     * }
     */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        items = new String[]{"a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a",
                "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a", "a"};

        View view = inflater.inflate(R.layout.fragment_all_stories, container, false);
        lv = (ListView) view.findViewById(R.id.listview);
        /*
        StoriesListAdapter sladapter = new StoriesListAdapter(this.getActivity(), items);
        lv.setAdapter(sladapter);*/
        new Thread(new Runnable() {

            @Override
            public void run() {
                StoriesListAdapter adapter = new StoriesListAdapter(getActivity(), items);
                lv.setAdapter(adapter);

            }
        }).start();

        return view;


    }

    // 0 : AllStorie    1: MyStories   2: FavsStories
    public void navigatorToSelector(int Selector) {
        navigatorResetColor();
        navigator[Selector].setBackgroundColor(getResources().getColor(R.color.primary));
        TextView v = (TextView) navigator[Selector].getChildAt(0);
        v.setTextColor(getResources().getColor(R.color.softGrey));

    }
    // Remet couleur de base
    public void navigatorResetColor(){
        for(int i=0 ; i < navigator.length ; i++) {
            navigator[i].setBackgroundColor(getResources().getColor(R.color.softGrey));
            TextView v = (TextView) navigator[i].getChildAt(0);
            v.setTextColor(getResources().getColor(R.color.primary));
        }
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        //Mettre action de back sur la fleche
        getActivity().findViewById(R.id.story_chooser_back_btn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        //On va get chacun des elements du navigator
        all_stories = (LinearLayout) getActivity().findViewById(R.id.all_story_page);
        my_stories = (LinearLayout) getActivity().findViewById(R.id.my_story_page);
        favs_stories = (LinearLayout) getActivity().findViewById(R.id.favs_story_page);

        //Tableau des LinearLayout(Navigator)
        navigator = new LinearLayout[]{all_stories, my_stories, favs_stories};

        //Set les actions des options du navigators
        all_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatorToSelector(0);
                //Thread qui remplace le listView
            }
        });


        my_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatorToSelector(1);
                //Thread qui remplace le listView
            }
        });


        favs_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatorToSelector(2);
                //Thread qui remplace le listView
            }
        });
        //Set a All Stories au depart
        navigatorToSelector(0);

    }

}
