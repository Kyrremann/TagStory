package no.uio.ifi.inf5261.tagstory;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.Database.Database;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StoryDetailFragment extends Fragment {

    // DummyContent.DummyItem mItem;
    private String AUTHOR;

    public StoryDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments().containsKey(Database.STORY_AUTHOR)) {
        	AUTHOR = getArguments().getString(Database.STORY_AUTHOR);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_story_detail, container, false);
        if (AUTHOR != null) {
            ((TextView) rootView.findViewById(R.id.story_detail)).setText(AUTHOR);
        }
        return rootView;
    }
}
