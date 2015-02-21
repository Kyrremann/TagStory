package no.tagstory.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import no.tagstory.R;

public class ReadStoriesFragment extends Fragment {

	public ReadStoriesFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_read_stories, container, false);
		((TextView) rootView.findViewById(R.id.section_label)).setText("Stories read");

		return rootView;
	}

	public static ReadStoriesFragment newInstance(int sectionNumber) {
		ReadStoriesFragment fragment = new ReadStoriesFragment();
		Bundle args = new Bundle();
		args.putInt("id", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
}
