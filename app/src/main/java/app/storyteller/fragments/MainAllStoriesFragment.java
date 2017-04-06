package app.storyteller.fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;

import app.storyteller.R;
import app.storyteller.StoryReaderActivity;
import app.storyteller.database.DBHandler;

import static app.storyteller.testing.MihaiTesting.testingStory;

/**
 * Created by Mihai on 2017-01-20.
 */

public class MainAllStoriesFragment extends Fragment implements AdapterView.OnItemClickListener{
    ListView lv;
    String[] titles;
    String[] authors;
    boolean[] favorites;

    LinearLayout loading_screen;
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        //init les tableaux avec le size de getAllStories (version bs)
        titles = new String[] {"A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience" };
        authors = new String[] {"Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents"};
        favorites = new boolean[titles.length];
        View view = inflater.inflate(R.layout.fragment_all_stories, container, false);
        lv = (ListView) view.findViewById(R.id.listview);
        initHeader(view);
        Publish p = new Publish();

        p.execute();



        return view;
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(),StoryReaderActivity.class);
        intent.putExtra("Title",titles[position]);
        intent.putExtra("Authors",authors[position]);
        intent.putExtra("Story","GET THE STORY AND PUT IT HERE GEE.");
        intent.putExtra("Favs",favorites[position]);



        startActivity(intent);
    }

    public void initHeader(View v){
        v.findViewById(R.id.story_chooser_back_lyt).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ((TextView)v.findViewById(R.id.app_header_title)).setText(getString(R.string.storie_list_header_title));
    }













    public class Publish extends AsyncTask{
        ProgressDialog proDialog;
        @Override
        protected Object doInBackground(Object[] params) {


            //Do DB fetch info et mettre dans un Objet ou des tableaux


            //logique pour get de la DB
            //getAllStories()
            //passer un tableau avec les Stories a l'adapter
            //passer le tableau des favorites de l'utilisateur a l'adapter
            //gerer l'extract du title + author dans l'adapter

            testingStory(getContext());
            DBHandler.openConnection(getContext());

            titles[0] = DBHandler.getStory(126).getDetails().getTitle();
            authors[0] = DBHandler.getStory(126).getCreator().getName();
            ArrayList<Integer> arrayList = DBHandler.getFavorites(127);
            favorites[0] = DBHandler.getStory(126).getId() == arrayList.get(0);
            DBHandler.closeConnection();
            //end


            return null;
        }

        @Override
        protected void onPostExecute(Object o) {
            super.onPostExecute(o);
            lv.removeAllViewsInLayout();
            StoriesListAdapter adapter = new StoriesListAdapter(getActivity(), titles, authors, favorites);
            lv.setOnItemClickListener(MainAllStoriesFragment.this);
            lv.setAdapter(adapter);
            proDialog.dismiss();
        }

        @Override
        protected void onProgressUpdate(Object[] values) {
            super.onProgressUpdate(values);
            //Loading screen


        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            proDialog = new ProgressDialog(getContext());
            proDialog.setMessage("LOADING");
            proDialog.setIndeterminate(false);
            proDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
            proDialog.setCancelable(false);
            proDialog.show();
        }
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
                Publish p = new Publish();
                p.execute();
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
