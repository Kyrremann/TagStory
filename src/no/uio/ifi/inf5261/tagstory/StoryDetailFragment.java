package no.uio.ifi.inf5261.tagstory;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.Database.Database;
import no.uio.ifi.inf5261.tagstory.story.Story;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

public class StoryDetailFragment extends Fragment {

	private Story story;

	public StoryDetailFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		if (getArguments().containsKey("STORY")) {
			story = (Story) getArguments().getSerializable("STORY");
		}
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_story_detail,
				container, false);
		if (story != null) {
			((TextView) rootView.findViewById(R.id.story_detail_author))
					.setText("Author: " + story.getAuthor());
			((TextView) rootView.findViewById(R.id.story_detail_age))
					.setText("Agegroup: " + story.getAgeGroup());
			((TextView) rootView.findViewById(R.id.story_detail_desc))
					.setText("Description:\n" + story.getDesc());
			((TextView) rootView.findViewById(R.id.story_detail_area))
					.setText("Area: " + story.getArea());
			((TextView) rootView.findViewById(R.id.story_detail_date))
					.setText("Date: " + story.getDate());
			((TextView) rootView.findViewById(R.id.story_detail_keywords))
					.setText("Keywords: " + story.getKeywords());
			((TextView) rootView.findViewById(R.id.story_detail_tagcount))
					.setText("Tag count: " + story.getTagCount());
			((TextView) rootView.findViewById(R.id.story_detail_genre))
					.setText("Genre: " + story.getGenre());
		}
		return rootView;
	}
}
