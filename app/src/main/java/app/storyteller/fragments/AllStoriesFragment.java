package app.storyteller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.ListFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import app.storyteller.R;
import app.storyteller.database.DBHandler;

/**
 * Created by Mihai on 2017-01-20.
 */

public class AllStoriesFragment extends Fragment {


    /**
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_all_stories, container, false);

        return rootView;
    }
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        String[] items = new String[] {"a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a",
                "a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a"};

        View view = inflater.inflate(R.layout.main_all_stories, container, false);

        ListView lv = (ListView) view.findViewById(R.id.stories_reader_list);
        StoriesListAdapter sladapter = new StoriesListAdapter(this.getActivity(), items);
        lv.setAdapter(sladapter);

        return view;

    }


    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);



    }

}