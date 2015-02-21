package no.tagstory.profile;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.adapters.StoryJsonAdapter;
import no.tagstory.honeycomb.StoryMarketListingActivityHoneycomb;
import no.tagstory.market.StoryMarketListingActivity;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.ImageLoaderUtils;
import no.tagstory.utils.http.SimpleStoryHandler;
import no.tagstory.utils.http.StoryProtocol;
import org.json.JSONArray;

public class WrittenStoriesFragment extends Fragment implements AdapterView.OnItemClickListener, SimpleStoryHandler.SimpleCallback {

	private ListView listView;
	private JSONArray stories;
	private StoryApplication storyApplication;

	public WrittenStoriesFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		storyApplication = (StoryApplication) getActivity().getApplication();
		if (storyApplication.isMarketStoriesEmptyOrOutdated()) {
			final SimpleStoryHandler handler = new SimpleStoryHandler(this);
			new Thread(new Runnable() {
				@Override
				public void run() {
					StoryProtocol.downloadNewStoriesToTheStoryApplication(getActivity().getApplicationContext(), handler);
				}
			}).start();
		}
		stories = storyApplication.getMarketStories();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image_list, container, false);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		listView.setAdapter(new StoryJsonAdapter(getActivity(), stories));
		listView.setOnItemClickListener(this);
		return rootView;
	}

	public static WrittenStoriesFragment newInstance() {
		return new WrittenStoriesFragment();
	}

	private void applyScrollListener() {
		listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), false, true));
	}

	@Override
	public void onResume() {
		super.onResume();
		applyScrollListener();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ImageLoaderUtils.AnimateFirstDisplayListener.displayedImages.clear();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		Intent intent = ClassVersionFactory.createIntent(getActivity().getApplicationContext(), StoryMarketListingActivityHoneycomb.class, StoryMarketListingActivity.class);
		intent.putExtra("json", stories.optString(position, ""));
		startActivity(intent);
	}

	@Override
	public void onMessageDone() {
		stories = storyApplication.getMarketStories();
		((StoryJsonAdapter) listView.getAdapter()).notifyDataSetChanged(stories);
	}

	@Override
	public void onMessageFail(int error) {

	}
}
