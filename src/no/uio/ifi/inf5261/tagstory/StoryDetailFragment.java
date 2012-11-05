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
			((TextView) rootView.findViewById(R.id.story_detail_title))
					.setText(story.getTitle());
			((TextView) rootView.findViewById(R.id.story_detail_author))
					.setText(story.getAuthor());
			((TextView) rootView.findViewById(R.id.story_detail_age))
					.setText("Agegroup: " + story.getAgeGroup());
			((TextView) rootView.findViewById(R.id.story_detail_desc))
					.setText(story.getDesc());
			((TextView) rootView.findViewById(R.id.story_detail_area))
					.setText("Area: " + story.getArea());
			((TextView) rootView.findViewById(R.id.story_detail_date))
					.setText(story.getDate());
			((TextView) rootView.findViewById(R.id.story_detail_keywords))
					.setText(story.getKeywords().toString());
			((TextView) rootView.findViewById(R.id.story_detail_tagcount))
					.setText(story.getTagCount() + " tags");
			((TextView) rootView.findViewById(R.id.story_detail_genre))
					.setText("Genre: " + story.getGenre());
		}
		return rootView;
	}
}
