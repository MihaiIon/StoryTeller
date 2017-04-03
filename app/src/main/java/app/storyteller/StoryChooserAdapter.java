package app.storyteller;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

/**
 * Created by Andre on 2017-04-03.
 */

public class StoryChooserAdapter extends BaseAdapter {

    private Context context;
    private String[] titles;
    private String[] previews;  //last sentence from string?
    private LayoutInflater inflater;

    public StoryChooserAdapter(Context context, String[] titles, String[] previews) {
        this.context = context;
        this.titles = titles;
        this.previews = previews;
        if(context != null)
            inflater = (LayoutInflater) context
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return 0;
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
        titretv.setText(this.titles[position]);

        //Va chercher text et modifie
        //Plus tard modifier pour ce qui a été prit dans BD
        TextView previewtv = (TextView) v.findViewById(R.id.chooser_preview);
        previewtv.setText(this.previews[position]);

        return v;
    }
}
