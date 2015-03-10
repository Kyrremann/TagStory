package no.tagstory.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import no.tagstory.R;
import no.tagstory.market.StoryMarketListingActivity;
import no.tagstory.utils.ImageLoaderUtils;
import no.tagstory.utils.StoryParser;
import no.tagstory.utils.http.StoryProtocol;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class StoryJsonAdapter extends BaseAdapter {

	private JSONArray stories;
	private LayoutInflater inflater;
	private ImageLoadingListener animateFirstListener = new ImageLoaderUtils.AnimateFirstDisplayListener();

	public StoryJsonAdapter(Context context, JSONArray stories) {
		this.stories = stories;
		inflater = LayoutInflater.from(context);
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

			holder.text.setText(jsonObject.getString(StoryParser.TITLE));

			String url = null;
			if (jsonObject.has(StoryParser.IMAGE)) {
				String imageUrl = jsonObject.getString(StoryParser.IMAGE);
				if (imageUrl.length() != 0) {
					url = StoryProtocol.SERVER_URL_IMAGES + imageUrl;
				}
			}

			ImageLoader.getInstance().displayImage(url, holder.image, ImageLoaderUtils.options, animateFirstListener);
		} catch (JSONException e) {
			holder.text.setText(R.string.market_error_item_list);
		}

		return view;
	}

	public void notifyDataSetChanged(JSONArray stories) {
		this.stories = stories;
		notifyDataSetChanged();
	}

	private static class ViewHolder {
		TextView text;
		ImageView image;
	}
}
