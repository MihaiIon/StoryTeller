package app.storyteller.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.ToggleButton;

import java.util.ArrayList;

import app.storyteller.R;
import app.storyteller.StoryReaderActivity;
import app.storyteller.api.Api;
import app.storyteller.api.ApiRequests;
import app.storyteller.database.DBHandler;
import app.storyteller.manager.AppManager;
import app.storyteller.models.Story;

/**
 * Created by Mihai on 2017-01-20.
 */

public class MainAllStoriesFragment extends Fragment implements AdapterView.OnItemClickListener{
    ListView lv;
    ArrayList<Story> favstories;
    ArrayList<Story> mystories;
    ArrayList<Story> stories;
    int current_nav; // 0:All 1:My 2:Favs
    static boolean[] favorites;

    private SwipeRefreshLayout swipeContainer;
    TextView emptyText;
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
        //titles = new String[] {"A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience","A walk through the woods","And there she comes","Gena's Legend" , "Simply put it's trivial", "My one and only","Terror in ComputerScience" };
        //authors = new String[] {"Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents","Jenny2009","EliteBoi","Gena","Gilles","A+","TheStudents"};
        //favorites = new boolean[titles.length];
        View view = inflater.inflate(R.layout.fragment_all_stories, container, false);
        emptyText = (TextView)view.findViewById(R.id.emptylist);
        lv = (ListView) view.findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
        initHeader(view);
        initSwipeContainer(view);

        current_nav =0;
        /*Publish p = new Publish();

        p.execute();*/
        fetchCompleteStories();

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchCompleteStories();
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(),StoryReaderActivity.class);
        switch (current_nav){
            case 0:
                intent.putExtra("Title",stories.get(position).getDetails().getTitle());
                intent.putExtra("Authors",stories.get(position).getCreator().getName());
                intent.putExtra("Story",stories.get(position).getContent());
                intent.putExtra("Favs",favorites[position]);
                intent.putExtra("Position",position);
                intent.putExtra("StoryID",stories.get(position).getId());
                startActivity(intent);
                break;
            case 1:
                intent.putExtra("Title",mystories.get(position).getDetails().getTitle());
                intent.putExtra("Authors",mystories.get(position).getCreator().getName());
                intent.putExtra("Story",mystories.get(position).getContent());
                intent.putExtra("Favs",favorites[position]);
                intent.putExtra("Position",position);
                intent.putExtra("StoryID",mystories.get(position).getId());
                startActivity(intent);
                break;
            case 2:
                intent.putExtra("Title",favstories.get(position).getDetails().getTitle());
                intent.putExtra("Authors",favstories.get(position).getCreator().getName());
                intent.putExtra("Story",favstories.get(position).getContent());
                intent.putExtra("Favs",favorites[position]);
                intent.putExtra("Position",position);
                intent.putExtra("StoryID",favstories.get(position).getId());
                startActivity(intent);
                break;
        }

    }

    public void initHeader(View v){
        v.findViewById(R.id.header_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ((TextView)v.findViewById(R.id.header_title)).setText(getString(R.string.storie_list_header_title));
    }


    private void initSwipeContainer(View v){
        swipeContainer = (SwipeRefreshLayout) v.findViewById(R.id.all_stories_swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                swipeContainer.setRefreshing(false);
                fetchCompleteStories();
            }
        });
    }

    /**
     * Get completed stories from API
     */
    private void fetchCompleteStories(){
        Api.executeRequest(ApiRequests.getCompletedStories(), this);
    }




    //-------------------------------------------------------------------
    // Methods

    /**
     *
     * @param list : List of...TODO
     */
    public void onCompletedStoriesFetched(ArrayList<Story> list) {
        // TODO.
        this.stories = list;
        boolean[] fav;

        fav = new boolean[list.size()];
        this.favorites = fav;


        // C'EST ICI QUE CA SE PASSE
        //MODIFIER POUR LES FAVORIES ET TOUT
        //VINCENT

        ArrayList<Integer> favsFromDB = new ArrayList<>(stories.size());
        DBHandler.openConnection(getContext());
        if (DBHandler.getFavoriteListSize() > 0) {
            favsFromDB = DBHandler.getFavorites(AppManager.getAccount().getId());
            for (int i = 0; i < favsFromDB.size(); i++) {
                for (int j = 0; j < list.size(); j++) {
                    if(list.get(j).getId() == favsFromDB.get(i)) {
                        favorites[j] = true;
                        break;
                    }
                }
            }
        }
        DBHandler.closeConnection();

        for (int i = 0; i < favsFromDB.size(); i++) {
            System.out.println(favsFromDB.get(i));
        }

        lv.setAdapter(new StoriesListAdapter(getContext(),list,favorites));

       switch(current_nav){
            case 0: // All
                allselected();
                break;
            case 1: // MyStories
                myselected();
                break;
           case 2: // Favs
               favoriteselected();
               break;
        }

    }

    private void ShowListView(){
        lv.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.INVISIBLE);
    }
    private void HideListView(){
        lv.setVisibility(View.INVISIBLE);
        emptyText.setVisibility(View.VISIBLE);
    }

    // 0 : AllStorie    1: MyStories   2: FavsStories
    public void navigatorToSelector(int Selector) {
        navigatorResetColor();
        navigator[Selector].setBackgroundColor(getResources().getColor(R.color.primary));
        TextView v = (TextView) navigator[Selector].getChildAt(0);
        v.setTextColor(getResources().getColor(R.color.softGrey));
        current_nav = Selector;
    }

    // Remet couleur de base
    public void navigatorResetColor(){
        for(int i=0 ; i < navigator.length ; i++) {
            navigator[i].setBackgroundColor(getResources().getColor(R.color.softGrey));
            TextView v = (TextView) navigator[i].getChildAt(0);
            v.setTextColor(getResources().getColor(R.color.primary));
        }
    }

    private void allselected()
    {
        if(stories.isEmpty()){
            HideListView();
            emptyText.setText(R.string.empty_all_story);
        }
        else {
            favorites = new boolean[stories.size()];
            ArrayList<Integer> favsFromDB = new ArrayList<>(stories.size());
            DBHandler.openConnection(getContext());
            if (DBHandler.getFavoriteListSize() > 0) {
                favsFromDB = DBHandler.getFavorites(AppManager.getAccount().getId());
                for (int i = 0; i < favsFromDB.size(); i++) {
                    for (int j = 0; j < stories.size(); j++) {
                        if(stories.get(j).getId() == favsFromDB.get(i)) {
                            favorites[j] = true;
                            break;
                        }
                    }
                }
            }
            DBHandler.closeConnection();
            lv.setAdapter(new StoriesListAdapter(getContext(), stories, favorites));
            ShowListView();
        }
    }
    private void myselected(){
        mystories = new ArrayList<Story>();
        for (int i=0 ; i<stories.size();i++) {
            int storyname = stories.get(i).getCreator().getId();
            int googlename = AppManager.getAccount().getId();
            if (storyname == googlename) {
                mystories.add(stories.get(i));
            }
        }
        if(mystories.isEmpty()) {
            HideListView();
            emptyText.setText(R.string.empty_my_story);
            lv.setAdapter(null);
        }
        else{
            favorites = new boolean[mystories.size()];
            ArrayList<Integer> favsFromDB = new ArrayList<>(mystories.size());
            DBHandler.openConnection(getContext());
            if (DBHandler.getFavoriteListSize() > 0) {
                favsFromDB = DBHandler.getFavorites(AppManager.getAccount().getId());
                for (int i = 0; i < favsFromDB.size(); i++) {
                    for (int j = 0; j < mystories.size(); j++) {
                        if(mystories.get(j).getId() == favsFromDB.get(i)) {
                            favorites[j] = true;
                            break;
                        }
                    }
                }
            }
            DBHandler.closeConnection();
            //
            lv.setAdapter(new StoriesListAdapter(getContext(),mystories,favorites));
            ShowListView();
        }
    }
    private void favoriteselected()
    {
        favstories = new ArrayList<Story>();
        ArrayList<Integer> favsFromDB = new ArrayList<>(stories.size());
        DBHandler.openConnection(getContext());
        favorites = new boolean[DBHandler.getFavoriteListSize()];

        if (DBHandler.getFavoriteListSize() > 0) {
            favsFromDB = DBHandler.getFavorites(AppManager.getAccount().getId());
            for (int i = 0; i < favsFromDB.size(); i++) {
                for (int j = 0; j < stories.size(); j++) {
                    if(stories.get(j).getId() == favsFromDB.get(i)) {
                        favstories.add(stories.get(j));
                        favorites[i] = true;
                        break;
                    }
                }
            }
            lv.setAdapter(new StoriesListAdapter(getContext(),favstories,favorites));
            ShowListView();
        }
        else {
            HideListView();
            emptyText.setText(R.string.empty_fav_story);
            lv.setAdapter(null);
        }
        DBHandler.closeConnection();




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
                allselected();
                //Thread qui remplace le listView
                /*Publish p = new Publish();
                p.execute();*/
            }
        });


        my_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatorToSelector(1);
                myselected();
                //Thread qui remplace le listView
            }
        });


        favs_stories.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigatorToSelector(2);
                favoriteselected();

                //Thread qui remplace le listView
            }
        });
        //Set a All Stories au depart
        navigatorToSelector(0);

    }



    static public void setFavs(int position, boolean check)
    {
        favorites[position] = check;
    }

    private class StoriesListAdapter extends BaseAdapter {

        private Context context;
        private String[] titles;
        private String[] authors;
        private boolean[] favorites; //true, false selon si le story au meme index est favorite
        private LayoutInflater inflater;


        public StoriesListAdapter(Context context, ArrayList<Story> stories, boolean[] favorites) {
            this.context = context;
            this.titles = new String[stories.size()];
            this.authors = new String[stories.size()];

            for (int i = 0; i < stories.size(); i++) {
                this.titles[i] = stories.get(i).getDetails().getTitle();
                this.authors[i] = stories.get(i).getCreator().getName();
            }

            this.favorites = favorites;
            if(context != null)
                inflater = (LayoutInflater) context
                        .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        }

        @Override
        public int getCount() {
            return titles.length;
        }

        @Override
        public Object getItem(int position) {
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            //return super.getView(position, convertView, parent);

       /* View rowView = inflater.inflate(R.layout.list_stories_element, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text_view_stories_reader);
        ToggleButton toggleButton = (ToggleButton) rowView.findViewById(R.id.toggle_button_favorite);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), position + " faved", Toast.LENGTH_SHORT).show();
            }
        });
YOLO GROS CHANGEMENT
        textView.setText(values[position]);
*/
            View v = convertView;

            //Modifie la row dans le listView
            v= inflater.inflate(R.layout.fragment_stories_list,parent,false);

            //Va chercher le toggle et change ces propriétés
            ToggleButton tb = (ToggleButton) v.findViewById(R.id.toggleButton);
            tb.setChecked(this.favorites[position]);

            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int playerId = AppManager.getAccount().getId();

                    switch (current_nav) {
                        case 0:
                            if (isChecked){
                                DBHandler.openConnection(getContext());
                                DBHandler.addFavorite(playerId,stories.get(position).getId());
                                DBHandler.closeConnection();
                                setFavs(position,true);
                            }
                            else{
                                DBHandler.openConnection(getContext());
                                DBHandler.removeFavorite(playerId,stories.get(position).getId());
                                DBHandler.closeConnection();
                                setFavs(position,false);
                            }
                            break;
                        case 1:
                            if (isChecked){
                                DBHandler.openConnection(getContext());
                                DBHandler.addFavorite(playerId,mystories.get(position).getId());
                                DBHandler.closeConnection();
                                setFavs(position,true);
                            }
                            else {
                                DBHandler.openConnection(getContext());
                                DBHandler.removeFavorite(playerId,mystories.get(position).getId());
                                DBHandler.closeConnection();
                                setFavs(position,false);
                            }
                            break;
                        case 2:
                            if (isChecked){
                                DBHandler.openConnection(getContext());
                                DBHandler.addFavorite(playerId,favstories.get(position).getId());
                                DBHandler.closeConnection();
                                setFavs(position,true);
                            }
                            else {
                                DBHandler.openConnection(getContext());
                                DBHandler.removeFavorite(playerId,favstories.get(position).getId());
                                DBHandler.closeConnection();
                                setFavs(position,false);
                            }
                            break;

                    }

                }
            });

            //Va chercher text et modifie
            //Plus tard modifier pour ce qui a été prit dans BD
            TextView titretv = (TextView) v.findViewById(R.id.title);
            titretv.setText(this.titles[position]);

            //Va chercher text et modifie
            //Plus tard modifier pour ce qui a été prit dans BD
            TextView authortv = (TextView) v.findViewById(R.id.completed_stories_list_item_author);
            authortv.setText(this.authors[position]);



            return v;

        }
    }

}
