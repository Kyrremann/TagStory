package no.tagstory.marked;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.RoundedBitmapDisplayer;
import no.tagstory.R;
import no.tagstory.utils.Database;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;

public class StoryMarkedListingActivity extends Activity {

	public static String SERVER_URL = "https://s3-eu-west-1.amazonaws.com/tagstory/images/";

	private static final int MESSAGE_DONE = 0;
	private static final int MESSAGE_FAIL_JSON = -1;
	private static final int MESSAGE_FAIL_HTTP = -2;

	private DisplayImageOptions options;
	private JSONObject jsonObject;
	private Handler handler;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_marked_listing);

		try {
			jsonObject = new JSONObject(getIntent().getStringExtra("json"));
			JSONObject story = jsonObject.getJSONObject("value");

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

			String url = "";
			if (story.has("image")) {
				url = story.getString("image");
			}
			if (url.length() == 0) {
				url = "placeimg_960_720_nature_1.jpg";
			}

			ImageLoader.getInstance().displayImage(SERVER_URL + url, imageView);

			title.setText(story.getString("title"));
			author.setText(story.getString("author"));
			description.setText(story.getString("description"));
		} catch (JSONException e) {
			e.printStackTrace();
			// TODO
		}
	}

	public void onClick(View view) {
		switch (view.getId()) {
			case R.id.download:
				final ProgressDialog progressDialog = new ProgressDialog(this);
				progressDialog.setCancelable(false);
				progressDialog.setMessage("Downloading story");
				progressDialog.show();
				// TODO Download story
				final String id = jsonObject.optString("id", "");
				// TODO Check for wrong ID
				handler = new Handler(new Handler.Callback() {
					@Override
					public boolean handleMessage(Message msg) {
						switch (msg.what) {
							case MESSAGE_DONE:
								progressDialog.cancel();
								break;
							case MESSAGE_FAIL_HTTP:
								Toast.makeText(getApplicationContext(), "Something went wrong with the http-connection", Toast.LENGTH_SHORT).show();
								break;
							case MESSAGE_FAIL_JSON:
								Toast.makeText(getApplicationContext(), "Something went wrong with the data", Toast.LENGTH_SHORT).show();
								break;
							default:
								break;
						}

						return true;
					}
				});
				new Thread(new Runnable() {
					@Override
					public void run() {
						try {
							HttpClient client = new DefaultHttpClient();
							String url = "http://tagstory.herokuapp.com/story/" + id + "/json";
							HttpGet get = new HttpGet(url);
							String content = client.execute(get, new BasicResponseHandler());

							JSONObject story = new JSONObject(content);
							JSONObject jsonObject = new JSONObject();
							jsonObject.put("story", story);
							String filename = story.getString("UUID");
							if (!filename.endsWith(".json")) {
								filename = filename.concat(".json");
							}
							FileOutputStream fileOutputStream = openFileOutput(filename, Context.MODE_PRIVATE);
							fileOutputStream.write(jsonObject.toString().getBytes());
							fileOutputStream.close();

							Database database = new Database(getApplicationContext());
							database.open();
							database.insertStory(story.getString("UUID"), story.getString("author"), story.getString("title"), "unspecified", ""); // story.getString("image")
							// TODO download images
						} catch (IOException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(MESSAGE_FAIL_HTTP);
						} catch (JSONException e) {
							e.printStackTrace();
							handler.sendEmptyMessage(MESSAGE_FAIL_JSON);
						} finally {
							handler.sendEmptyMessage(MESSAGE_DONE);
						}
					}
				}).start();
				break;
		}
	}
}
