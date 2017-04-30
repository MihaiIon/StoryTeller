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

public class MainAllStoriesFragment extends Fragment implements AdapterView.OnItemClickListener {
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


    @Override
    public View onCreateView(final LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_all_stories, container, false);

        //Init l'interface
        initEmptyText(view);
        initListeView(view);
        initHeader(view);
        initSwipeContainer(view);
        //Va chercher les histoires completer dans la BD externe
        fetchCompleteStories();
        current_nav = 0; // selection du navigation bar maison
        return view;
    }

    private void initEmptyText(View view) {
        emptyText = (TextView) view.findViewById(R.id.emptylist);
    }

    private void initListeView(View view)
    {
        lv = (ListView) view.findViewById(R.id.listview);
        lv.setOnItemClickListener(this);
    }

    private void initHeader(View v){
        v.findViewById(R.id.header_back_arrow).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });
        ((TextView)v.findViewById(R.id.header_title)).setText(getString(R.string.story_list_header_title));
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


    //Ouverture du Reader de l'histoire cliquée
    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        Intent intent = new Intent(getContext(),StoryReaderActivity.class);
        switch (current_nav){
            case 0:
                InitExtra(intent,stories,position);
                break;
            case 1:
                InitExtra(intent,mystories,position);
                break;
            case 2:
                InitExtra(intent,favstories,position);
                break;
        }
    }
    // Fonction qui va met les extras et start le reader
    private void InitExtra(Intent intent,ArrayList<Story> liste,int position){
        intent.putExtra("Title",liste.get(position).getDetails().getTitle());
        intent.putExtra("Authors",liste.get(position).getCreator().getName());
        intent.putExtra("Story",liste.get(position).getContent());
        intent.putExtra("Favs",favorites[position]);
        intent.putExtra("Position",position);
        intent.putExtra("StoryID",liste.get(position).getId());
        startActivity(intent);
    }

    //Get completed stories from API
    private void fetchCompleteStories(){
        Api.executeRequest(ApiRequests.getCompletedStories(), this);
    }


    /**
     * @param list : List of CompledtedStories
     *  Fonction appellé lorsqu'on a toutes les histoires de la BD
     */
    public void onCompletedStoriesFetched(ArrayList<Story> list) {

        this.stories = list;
        boolean[] fav;

        fav = new boolean[list.size()];
        this.favorites = fav;

        //Initiation des favories
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

        lv.setAdapter(new StoriesListAdapter(getContext(),list,favorites));

        // Créer nouveau tableau de vafories selon le navigation bar maison
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

    //Lorsque l'onglet All est sélectionné
    private void allselected()
    {
        if(stories.isEmpty()){
            HideListView();
            emptyText.setText(R.string.empty_all_story);
        } else {
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
    //Lorsque l'onglet MyStory est sélectionné
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
        } else{
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

    //Lorsque l'onglet favorie est sélectionné
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
        } else {
            HideListView();
            emptyText.setText(R.string.empty_fav_story);
            lv.setAdapter(null);
        }
        DBHandler.closeConnection();
    }

    //Afficher le ListView et cacher le text
    private void ShowListView(){
        lv.setVisibility(View.VISIBLE);
        emptyText.setVisibility(View.INVISIBLE);
    }
    //Cacher le ListView et afficher le text
    private void HideListView(){
        lv.setVisibility(View.INVISIBLE);
        emptyText.setVisibility(View.VISIBLE);
    }

    // 0 : AllStorie    1: MyStories   2: FavsStories
    // Recolore le navigation bar Maison
    public void navigatorToSelector(int Selector) {
        navigatorResetColor();
        navigator[Selector].setBackgroundColor(getResources().getColor(R.color.primary));
        TextView v = (TextView) navigator[Selector].getChildAt(0);
        v.setTextColor(getResources().getColor(R.color.softGrey));
        current_nav = Selector;
    }

    // Remet couleur de base au navigation bar maison
    public void navigatorResetColor(){
        for(int i=0 ; i < navigator.length ; i++) {
            navigator[i].setBackgroundColor(getResources().getColor(R.color.softGrey));
            TextView v = (TextView) navigator[i].getChildAt(0);
            v.setTextColor(getResources().getColor(R.color.primary));
        }
    }

    //Init le navigation bar Maison
    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

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


    //change le favorie dans le tableau internet
    static public void setFavs(int position, boolean check)
    {
        favorites[position] = check;
    }

    //Adapter pour le ListView de CompletedStories
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
            View v = convertView;

            //Modifie la row dans le listView
            v= inflater.inflate(R.layout.fragment_stories_list,parent,false);

            //Va chercher le toggle et change ces propriétés
            ToggleButton tb = (ToggleButton) v.findViewById(R.id.toggleButton);
            tb.setChecked(this.favorites[position]);

            //action de click quand on change un de nos favories
            tb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                    int playerId = AppManager.getAccount().getId();

                    switch (current_nav) {
                        case 0:
                            ChangeFavoriteState(isChecked,playerId,stories,position);
                            break;
                        case 1:
                            ChangeFavoriteState(isChecked,playerId,mystories,position);
                            break;
                        case 2:
                            ChangeFavoriteState(isChecked,playerId,favstories,position);
                            break;
                    }

                }
            });

            //Va chercher text et modifie
            TextView titretv = (TextView) v.findViewById(R.id.title);
            titretv.setText(this.titles[position]);

            //Va chercher text et modifie
            TextView authortv = (TextView) v.findViewById(R.id.completed_stories_list_item_author);
            authortv.setText(this.authors[position]);

            return v;
        }


        //Sert a update un favories dans la BD internet
        private void ChangeFavoriteState(boolean isChecked,int playerId, ArrayList<Story> liste, int position){
            if (isChecked){
                DBHandler.openConnection(getContext());
                DBHandler.addFavorite(playerId,liste.get(position).getId());
                DBHandler.closeConnection();
                setFavs(position,true);
            }
            else {
                DBHandler.openConnection(getContext());
                DBHandler.removeFavorite(playerId,liste.get(position).getId());
                DBHandler.closeConnection();
                setFavs(position,false);
            }
        }
    }

}
