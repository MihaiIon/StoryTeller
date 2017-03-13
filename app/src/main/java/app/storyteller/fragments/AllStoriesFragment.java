package app.storyteller.fragments;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import app.storyteller.R;

/**
 * Created by Mihai on 2017-01-20.
 */

public class AllStoriesFragment extends Fragment {@Override
public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    ViewGroup rootView = (ViewGroup) inflater.inflate(R.layout.main_all_stories, container, false);
    return rootView;
}
}
