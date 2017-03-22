package app.storyteller.fragments;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import android.widget.BaseAdapter;
import android.widget.TextView;
import android.widget.ToggleButton;


import app.storyteller.R;


/**
 * Created by Andre on 2017-03-20.
 */

public class StoriesListAdapter extends BaseAdapter{

    private final Context context;
    private final String[] values;
    private LayoutInflater inflater;


    public StoriesListAdapter(Context context, String[] values) {
        this.context = context;
        this.values = values;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return values.length;
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
        v= inflater.inflate(R.layout.list_stories,parent,false);

        //Va chercher le toggle et change ces propriétés
        ToggleButton tb = (ToggleButton) v.findViewById(R.id.toggleButton);
        tb.setText(null);
        tb.setTextOn(null);
        tb.setTextOff(null);
        tb.setBackgroundResource(R.drawable.mytogglebutton);


        //Va chercher text et modifie
        //Plus tard modifier pour ce qui a été prit dans BD
        TextView titretv = (TextView) v.findViewById(R.id.title);
        titretv.setText("123456789012345678901234 ");

        //Va chercher text et modifie
        //Plus tard modifier pour ce qui a été prit dans BD
        TextView authortv = (TextView) v.findViewById(R.id.smallDesc);
        authortv.setText("Autheur : You MOFOS");



        return v;

    }
}
