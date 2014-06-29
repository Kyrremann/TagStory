package no.tagstory.marked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.marked.sections.NewStorySectionFragment;

public class SectionsPagerAdapterImpl extends FragmentPagerAdapter {

	private static final String[] tab_titles = new String[] { "New", "Popular", "Hand-picked" };

	public SectionsPagerAdapterImpl(FragmentManager fragmentManager) {
		super(fragmentManager);
	}

	@Override
	public Fragment getItem(int i) {
		switch (i) {
			case 0:
				// The first section of the app is the most interesting -- it offers
				// a launchpad into the other demonstrations in this example application.
				return new LaunchpadSectionFragment();
			case 1:
				return new NewStorySectionFragment();

			default:
				// The other sections of the app are dummy placeholders.
				Fragment fragment = new DummySectionFragment();
				Bundle args = new Bundle();
				args.putInt(DummySectionFragment.ARG_SECTION_NUMBER, i + 1);
				fragment.setArguments(args);
				return fragment;
		}
	}

	@Override
	public int getCount() {
		return 3;
	}

	@Override
	public CharSequence getPageTitle(int position) {
		return tab_titles[position];
	}

	/**
	 * A fragment that launches other parts of the demo application.
	 */
	public static class LaunchpadSectionFragment extends Fragment {

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_launchpad, container, false);

			// Demonstration of a collection-browsing activity.
			rootView.findViewById(R.id.demo_collection_button)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							Intent intent = new Intent(getActivity(), CollectionDemoActivity.class);
							startActivity(intent);
						}
					});

			// Demonstration of navigating to external activities.
			rootView.findViewById(R.id.demo_external_activity)
					.setOnClickListener(new View.OnClickListener() {
						@Override
						public void onClick(View view) {
							// Create an intent that asks the user to pick a photo, but using
							// FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET, ensures that relaunching
							// the application from the device home screen does not return
							// to the external activity.
							Intent externalActivityIntent = new Intent(Intent.ACTION_PICK);
							externalActivityIntent.setType("image/*");
							externalActivityIntent.addFlags(
									Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
							startActivity(externalActivityIntent);
						}
					});

			return rootView;
		}
	}

	/**
	 * A dummy fragment representing a section of the app, but that simply displays dummy text.
	 */
	public static class DummySectionFragment extends Fragment {

		public static final String ARG_SECTION_NUMBER = "section_number";

		@Override
		public View onCreateView(LayoutInflater inflater, ViewGroup container,
		                         Bundle savedInstanceState) {
			View rootView = inflater.inflate(R.layout.fragment_section_dummy, container, false);
			Bundle args = getArguments();
			((TextView) rootView.findViewById(android.R.id.text1)).setText(
					getString(R.string.dummy_section_text, args.getInt(ARG_SECTION_NUMBER)));
			return rootView;
		}
	}
}
