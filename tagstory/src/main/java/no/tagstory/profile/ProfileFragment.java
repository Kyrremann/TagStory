package no.tagstory.profile;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import no.tagstory.R;

public class ProfileFragment extends Fragment {

	public ProfileFragment() {
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
//		((TextView) rootView.findViewById(R.id.section_label)).setText("Profile");

		return rootView;
	}

	public static ProfileFragment newInstance(int sectionNumber) {
		ProfileFragment fragment = new ProfileFragment();
		Bundle args = new Bundle();
		args.putInt("id", sectionNumber);
		fragment.setArguments(args);
		return fragment;
	}
}
