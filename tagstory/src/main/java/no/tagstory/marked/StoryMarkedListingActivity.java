package no.tagstory.marked;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;
import com.amazonaws.mobileconnectors.s3.transfermanager.Download;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.nostra13.universalimageloader.core.ImageLoader;
import no.tagstory.R;
import no.tagstory.StoryDetailActivity;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.story.StoryManager;
import no.tagstory.utils.ClassVersionFactory;
import no.tagstory.utils.Database;
import no.tagstory.utils.StoryParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

public class StoryMarkedListingActivity extends Activity {

	private static String SERVER_URL = "https://s3-eu-west-1.amazonaws.com/tagstory/";
	private static String IMAGES_FOLDER = "images/";
	private static String AUDIO_FOLDER = "audio/";
	public static String SERVER_URL_IMAGES = SERVER_URL + IMAGES_FOLDER;
	public static String SERVER_URL_AUDIO = SERVER_URL + AUDIO_FOLDER;

	private static final int MESSAGE_DONE = 0;
	private static final int MESSAGE_FAIL_JSON = -1;
	private static final int MESSAGE_FAIL_HTTP = -2;
	private static final int MESSAGE_FAILED = -3;
	private static final int MESSAGE_INFO = 1;

	private JSONObject storyDetail;
	private boolean isDownloaded;
	private String storyUUID;

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
			storyUUID = storyDetailValues.getString("UUID");
			StoryManager storyManager = new StoryManager(this);
			if (storyManager.hasStory(storyUUID)) {
				setButtonToStartStory();
			}
			storyManager.closeDatabase();

			String url = "";
			if (storyDetailValues.has("image")) {
				url = storyDetailValues.getString("image");
			}
			if (url.length() == 0) {
				url = "placeimg_960_720_nature_1.jpg";
			}

			ImageLoader.getInstance().displayImage(SERVER_URL_IMAGES + url, imageView);

			title.setText(storyDetailValues.getString("title"));
			author.setText(storyDetailValues.getString("author"));
			description.setText(storyDetailValues.getString("description"));

			setTitle(storyDetailValues.getString("title"));
		} catch (JSONException e) {
			e.printStackTrace();
			// TODO
		}
	}

	private void setButtonToStartStory() {
		((TextView) findViewById(R.id.download)).setText(R.string.marked_go_to_story);
		isDownloaded = true;
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.download:
				if (isDownloaded) {
					Intent detailIntent = ClassVersionFactory.createIntent(getApplicationContext(),
							StoryDetailActivityHoneycomb.class, StoryDetailActivity.class);
					detailIntent.putExtra(Database.STORY_ID, storyUUID);
					startActivity(detailIntent);
				} else {
					downloadStory();
				}
				break;
		}
	}

	private void downloadStory() {
		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage("Downloading story");
		progressDialog.show();
		final Handler handler = new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case MESSAGE_DONE:
						progressDialog.cancel();
						setButtonToStartStory();
						break;
					case MESSAGE_FAIL_HTTP:
						Toast.makeText(getApplicationContext(), "Something went wrong with the http-connection", Toast.LENGTH_SHORT).show();
						break;
					case MESSAGE_FAIL_JSON:
						Toast.makeText(getApplicationContext(), "Something went wrong with the data", Toast.LENGTH_SHORT).show();
						break;
					case MESSAGE_INFO:
						progressDialog.setMessage("Downloading: " + msg.arg1 + "%");
					default:
						break;
				}

				return true;
			}
		});

		final String storyIdServerside = storyDetail.optString("id", "");
		// TODO Check for wrong ID
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					String url = "http://tagstory.herokuapp.com/story/" + storyIdServerside + "/json";
					HttpGet get = new HttpGet(url);
					String content = client.execute(get, new BasicResponseHandler());

					JSONObject storyServerside = new JSONObject(content);

					JSONObject storyObject = storyServerside.getJSONObject("story");
					String filename = storyObject.getString("UUID");
					if (!filename.endsWith(".json")) {
						filename = filename.concat(".json");
					}
					FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
					fileOutputStream.write(storyServerside.toString().getBytes());
					fileOutputStream.close();

					// TODO: download images
					downloadAssets(storyObject, handler);
					// TODO: Change button to 'play story'
					Database database = new Database(getApplicationContext());
					database.open();
					database.insertStory(storyObject.getString("UUID"), storyObject.getString("author"),
							storyObject.getString("title"), storyObject.getString("area"),
							storyObject.getString("image"));
				} catch (IOException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(MESSAGE_FAIL_HTTP);
				} catch (JSONException e) {
					e.printStackTrace();
					handler.sendEmptyMessage(MESSAGE_FAIL_JSON);
				} catch (Exception e) {
					e.printStackTrace();
					handler.sendEmptyMessage(MESSAGE_FAILED);
				} finally {
					handler.sendEmptyMessage(MESSAGE_DONE);
				}
			}
		}).start();
	}

	private void downloadAssets(JSONObject storyObject, Handler handler) throws JSONException, IOException {
		CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
				this,
				"eu-west-1:0524ea09-ca76-4e85-bc4a-a48b98e6b1f4",
				Regions.EU_WEST_1);
		TransferManager transferManager = new TransferManager(cognitoProvider);
		transferManager.getAmazonS3Client().setRegion(Region.getRegion(Regions.EU_WEST_1));

		downloadAsset(IMAGES_FOLDER, storyObject.optString(StoryParser.IMAGE, ""), transferManager, handler);

		JSONObject tags = storyObject.getJSONObject(StoryParser.TAGS);
		Iterator<String> keys = tags.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			JSONObject tag = tags.getJSONObject(key);
			if (tag.has(StoryParser.TAG_OPTIONS)) {
				JSONObject options = tag.getJSONObject(StoryParser.TAG_OPTIONS);
				Iterator<String> optionKeys = options.keys();
				while (optionKeys.hasNext()) {
					String optionKey = optionKeys.next();
					JSONObject option = options.getJSONObject(optionKey);
					if (option.has(StoryParser.HINT_IMAGE_SOURCE)) {
						downloadAsset(IMAGES_FOLDER, option.optString(StoryParser.HINT_IMAGE_SOURCE, ""), transferManager, handler);
					}
					if (option.has(StoryParser.HINT_SOUND_SOURCE)) {
						downloadAsset(AUDIO_FOLDER, option.optString(StoryParser.HINT_SOUND_SOURCE, ""), transferManager, handler);
					}
				}
			}
		}
	}

	// TODO watch out for errors
	private boolean downloadAsset(String serverUrl, String name, TransferManager transferManager, Handler handler) {
		if (StringUtil.isBlank(name)) {
			return false;
		}

		Download download = transferManager.download("tagstory", serverUrl + name, getFileStreamPath(name));
		while (!download.isDone()) {
			Message message = new Message();
			message.what = MESSAGE_INFO;
			message.arg1 = (int) download.getProgress().getPercentTransferred();
			handler.sendMessage(message);
			try {
				Thread.sleep(250);
			} catch (InterruptedException e) {
			}
		}
		Message message = new Message();
		message.what = MESSAGE_INFO;
		message.arg1 = 100;
		handler.sendMessage(message);

		return true;
	}
}
