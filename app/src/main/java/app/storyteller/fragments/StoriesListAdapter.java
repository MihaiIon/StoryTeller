package app.storyteller.fragments;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import app.storyteller.R;


/**
 * Created by Andre on 2017-03-20.
 */

public class StoriesListAdapter extends ArrayAdapter<String> {

    private final Context context;
    private final String[] values;

    public StoriesListAdapter(Context context, String[] values) {
        super(context, R.layout.main_all_stories, values);
        this.context = context;
        this.values = values;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        //return super.getView(position, convertView, parent);
        LayoutInflater inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.list_stories_element, parent, false);
        TextView textView = (TextView) rowView.findViewById(R.id.text_view_stories_reader);
        ToggleButton toggleButton = (ToggleButton) rowView.findViewById(R.id.toggle_button_favorite);

        toggleButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getContext(), position + " faved", Toast.LENGTH_SHORT).show();
            }
        });

        textView.setText(values[position]);

        return rowView;

    }
}
