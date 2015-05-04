package no.tagstory.market;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;
import com.nostra13.universalimageloader.core.ImageLoader;
import no.tagstory.R;
import no.tagstory.StoryDetailActivity;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.story.StoryManager;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.StoryParser;
import no.tagstory.utils.http.SimpleStoryHandler;
import no.tagstory.utils.http.StoryProtocol;
import org.json.JSONException;
import org.json.JSONObject;

public class StoryMarketListingActivity extends Activity implements SimpleStoryHandler.SimpleCallback {

	private JSONObject storyDetail;
	private boolean isDownloaded;
	private boolean isOutdated;
	private String storyUUID;
	private ProgressDialog progressDialog;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marked_listing);

		try {
			storyDetail = new JSONObject(getIntent().getStringExtra("json"));
			JSONObject storyDetailValues = storyDetail.getJSONObject("value");

			ImageView imageView = (ImageView) findViewById(R.id.image);
			TextView title = (TextView) findViewById(R.id.title);
			TextView author = (TextView) findViewById(R.id.author);
			TextView description = (TextView) findViewById(R.id.description);
			storyUUID = storyDetailValues.getString(StoryParser.UUID);
			setButtonText(storyDetailValues.getInt(StoryParser.VERSION));

			String url = "";
			if (storyDetailValues.has(StoryParser.IMAGE)) {
				url = storyDetailValues.getString(StoryParser.IMAGE);
			}
			if (url.length() == 0) {
				url = "placeimg_960_720_nature_1.jpg";
			}

			ImageLoader.getInstance().displayImage(StoryProtocol.SERVER_URL_IMAGES + url, imageView);

			String storyTitle = storyDetailValues.getString(StoryParser.TITLE);
			title.setText(storyTitle);
			author.setText(storyDetailValues.getString(StoryParser.AUTHOR));
			description.setText(storyDetailValues.getString(StoryParser.DESCRIPTION));

			setTitle(storyTitle);
		} catch (JSONException e) {
			finish();
		}
	}

	private void setButtonText(int storyVersion) {
		StoryManager storyManager = new StoryManager(this);
		if (storyManager.hasStory(storyUUID)) {
			if (storyManager.isStoryOutdated(storyUUID, storyVersion)) {
				setButtonToUpdateStory();
			} else {
				setButtonToStartStory();
			}
		}
		storyManager.closeDatabase();
	}

	private void setButtonToUpdateStory() {
		((TextView) findViewById(R.id.download)).setText(R.string.market_update_story);
		isDownloaded = false;
		isOutdated = true;
	}

	private void setButtonToStartStory() {
		((TextView) findViewById(R.id.download)).setText(R.string.market_go_to_story);
		isDownloaded = true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.download:
				if (isDownloaded) {
					Intent detailIntent = ClassVersionFactory.createIntent(getApplicationContext(),
							StoryDetailActivityHoneycomb.class, StoryDetailActivity.class);
					detailIntent.putExtra(StoryParser.UUID, storyUUID);
					startActivity(detailIntent);
				} else if (isOutdated) {
					deleteStory();
					downloadStory();
				} else {
					downloadStory();
				}
				break;
		}
	}

	private void deleteStory() {
		StoryManager storyManager = new StoryManager(this);
		storyManager.deleteStory(storyUUID);
		storyManager.closeDatabase();
	}

	private void downloadStory() {
		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.market_info_downloading_story));
		progressDialog.show();
		final Handler handler = new SimpleStoryHandler(this);

		final String storyIdServerside = storyDetail.optString("id", "");
		if (StringUtil.isBlank(storyIdServerside)) {
			handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_FAILED);
			return;
		}

		new Thread(new Runnable() {
			@Override
			public void run() {
				StoryProtocol.downloadStory(getApplicationContext(), handler, storyIdServerside);
			}
		}).start();
	}

	@Override
	public void onMessageDone() {
		progressDialog.cancel();
		setButtonToStartStory();
	}

	@Override
	public void onMessageInfo(int arg1, int arg2) {
		progressDialog.setMessage(getString(R.string.market_info_downloading_assets) + arg1 + "%");
	}

	@Override
	public void onMessageFail(int error) {
		switch (error) {
			case SimpleStoryHandler.MESSAGE_FAIL_HTTP:
				Toast.makeText(getApplicationContext(), getString(R.string.market_error_http), Toast.LENGTH_SHORT).show();
				break;
			case SimpleStoryHandler.MESSAGE_FAIL_JSON:
				Toast.makeText(getApplicationContext(), getString(R.string.market_error_data), Toast.LENGTH_SHORT).show();
				break;
		}
	}
}