package app.storyteller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import app.storyteller.R;

/**
 * Created by Mihai on 2017-01-20.
 */

public class MainAllStoriesFragment extends Fragment {
    ListView lv;
    String[] items;
    /**
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.fragment_all_stories, container, false);

        return rootView;
    }
    */
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        items = new String[] {"a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a",
                "a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a","a"};

        View view = inflater.inflate(R.layout.fragment_all_stories, container, false);
        lv = (ListView) view.findViewById(R.id.listview);
        /*
        StoriesListAdapter sladapter = new StoriesListAdapter(this.getActivity(), items);
        lv.setAdapter(sladapter);*/
        new Thread(new Runnable(){

            @Override
            public void run() {
                StoriesListAdapter adapter = new StoriesListAdapter(getActivity(),items);
                lv.setAdapter(adapter);

            }
        }).start();



        return view;


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



        getActivity().findViewById(R.id.all_story_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"All Stories",Toast.LENGTH_SHORT).show();
            }
        });
        getActivity().findViewById(R.id.my_story_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"My Stories",Toast.LENGTH_SHORT).show();
            }
        });
        getActivity().findViewById(R.id.favs_story_page).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(),"Favs Stories",Toast.LENGTH_SHORT).show();
            }
        });


    }

}

/*
* Tentative faites pour show/hide action bar
* 1. OnResume (Fragment) - change rien
* 2. OnViewCreated (Fragment) - app crash
* 3. Switch Case getItem (MainAcvitity) - Fonctionne a moitier
* 4. OnCreate (Fragment) - app crash
* */