package app.storyteller;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import app.storyteller.models.Story;

/**
 * Created by Andre on 2017-04-03.
 */

public class StoryChooserAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<Story> stories;
    private LayoutInflater inflater;

    public StoryChooserAdapter(Context context, ArrayList<Story> stories) {
        this.context = context;
        this.stories = stories;
        if(context != null)
            inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return stories.size();
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
    public View getView(int position, View convertView, ViewGroup parent) {
        //Modifie la row dans le listView
        View v = convertView;

        v = inflater.inflate(R.layout.story_chooser_list_item,parent,false);

        //Va chercher text et modifie
        //Plus tard modifier pour ce qui a été prit dans BD
        TextView titretv = (TextView) v.findViewById(R.id.chooser_title);
        titretv.setText(this.stories.get(position).getDetails().getTitle());

        //Va chercher text et modifie
        //Plus tard modifier pour ce qui a été prit dans BD
        TextView previewtv = (TextView) v.findViewById(R.id.chooser_preview);

        //get last sentence, keep 5 last words
        int indexOfLastSentence = this.stories.get(position).getSentences().size() - 1;
        String[] arrayLastSentence;
        String lastSentence = this.stories.get(position).getSentences().get(indexOfLastSentence).getContent();
        arrayLastSentence = lastSentence.split(" ");

        if (arrayLastSentence.length > 5) {
            lastSentence = "...";
            for (int i = arrayLastSentence.length - 5; i < arrayLastSentence.length; i++) {
                lastSentence += arrayLastSentence[i] + " ";
            }
            lastSentence = lastSentence.substring(0,lastSentence.length() - 1);
        }

        previewtv.setText(lastSentence);

        //Get text from array
        TextView themetv = (TextView) v.findViewById(R.id.chooser_theme);
        themetv.setText(this.stories.get(position).getDetails().getTheme());
        //set text color
        //color dictionnary?

        return v;
    }
}
