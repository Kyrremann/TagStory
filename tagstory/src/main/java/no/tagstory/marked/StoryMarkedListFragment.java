package no.tagstory.marked;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.*;
import android.widget.AdapterView.OnItemClickListener;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.honeycomb.StoryMarkedListingActivityHoneycomb;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.ImageLoaderUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoryMarkedListFragment extends Fragment implements OnItemClickListener {

	private AbsListView listView;
	private JSONArray stories;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		stories = ((StoryApplication) getActivity().getApplication()).getMarkedstories();
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		View rootView = inflater.inflate(R.layout.fragment_image_list, container, false);
		listView = (ListView) rootView.findViewById(android.R.id.list);
		((ListView) listView).setAdapter(new ImageAdapter());
		listView.setOnItemClickListener(this);
		return rootView;
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
		Intent intent = ClassVersionFactory.createIntent(getActivity().getApplicationContext(), StoryMarkedListingActivityHoneycomb.class, StoryMarkedListingActivity.class);
		intent.putExtra("json", stories.optString(position, ""));
		startActivity(intent);
	}

	private static class ViewHolder {
		TextView text;
		ImageView image;
	}

	class ImageAdapter extends BaseAdapter {

		private LayoutInflater inflater;
		private ImageLoadingListener animateFirstListener = new ImageLoaderUtils.AnimateFirstDisplayListener();

		ImageAdapter() {
			inflater = LayoutInflater.from(getActivity());
		}

		@Override
		public int getCount() {
			return stories.length();
		}

		@Override
		public Object getItem(int position) {
			return position;
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			View view = convertView;
			final ViewHolder holder;
			if (convertView == null) {
				view = inflater.inflate(R.layout.item_list_image, parent, false);
				holder = new ViewHolder();
				holder.text = (TextView) view.findViewById(R.id.title);
				holder.image = (ImageView) view.findViewById(R.id.image);
				view.setTag(holder);
			} else {
				holder = (ViewHolder) view.getTag();
			}

			try {
				JSONObject jsonObject = stories.getJSONObject(position).getJSONObject("value");

				holder.text.setText(jsonObject.getString("title"));

				String url = null;
				if (jsonObject.has("image")) {
					String imageUrl = jsonObject.getString("image");
					if (imageUrl.length() != 0) {
						url = StoryMarkedListingActivity.SERVER_URL_IMAGES + imageUrl;
					}
				}

				ImageLoader.getInstance().displayImage(url, holder.image, ImageLoaderUtils.options, animateFirstListener);
			} catch (JSONException e) {
				holder.text.setText(R.string.marked_error_item_list);
			}

			return view;
		}
	}
}
