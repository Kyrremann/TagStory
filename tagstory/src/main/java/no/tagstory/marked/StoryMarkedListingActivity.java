package no.tagstory.marked;

import android.app.Activity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import no.tagstory.R;
import org.json.JSONException;
import org.json.JSONObject;

public class StoryMarkedListingActivity extends Activity {

	private DisplayImageOptions options;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marked_listing);

		try {
			JSONObject jsonObject = new JSONObject(getIntent().getStringExtra("json")).getJSONObject("value");

			options = new DisplayImageOptions.Builder()
					.showImageOnLoading(R.drawable.ic_stub)
					.showImageForEmptyUri(R.drawable.ic_empty)
					.showImageOnFail(R.drawable.ic_error)
					.cacheInMemory(true)
					.cacheOnDisk(true)
					.considerExifParams(true)
					.displayer(new RoundedBitmapDisplayer(20))
					.build();

			ImageView imageView = (ImageView) findViewById(R.id.story_image);
			TextView title = (TextView) findViewById(R.id.title);
			TextView author = (TextView) findViewById(R.id.author);
			TextView description = (TextView) findViewById(R.id.description);

			String url = "http://www.2k3.org/wp-content/uploads/Screenshot-from-2014-10-27-194657-980x761.png";
			ImageLoader.getInstance().displayImage(url, imageView);

			title.setText(jsonObject.getString("title"));
			author.setText(jsonObject.getString("author"));
			description.setText(jsonObject.getString("description"));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.download:
				// TODO Download story
				break;
		}
	}
}
