package no.tagstory.utils.http;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.amazonmobileanalytics.internal.core.util.StringUtil;
import com.amazonaws.mobileconnectors.s3.transfermanager.Download;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.utils.Database;
import no.tagstory.utils.StoryParser;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Locale;

public class StoryProtocol {

	private static final String LOG = "STORYPROTOCOL";

	private static final String BUCKET = "tagstory";
	private static final String SERVER_URL = "https://s3-eu-west-1.amazonaws.com/tagstory/";
	private static final String IMAGES_FOLDER = "images/";
	private static final String AUDIO_FOLDER = "audio/";

	public static final String SERVER_URL_IMAGES = SERVER_URL + IMAGES_FOLDER;
	public static final String SERVER_URL_AUDIO = SERVER_URL + AUDIO_FOLDER;

	public static void downloadNewStoriesToTheStoryApplication(Context context) {
		downloadNewStoriesToTheStoryApplication(context, null);
	}

	public static void downloadNewStoriesToTheStoryApplication(Context context, Handler handler) {
		try {
			HttpClient client = new DefaultHttpClient();
			HttpGet get = new HttpGet(context.getString(R.string.market_api_stories));
			String content = client.execute(get, new BasicResponseHandler());

			((StoryApplication) context).setMarkedstories(new JSONArray(content));
		} catch (IOException e) {
			if (handler != null) {
				handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_FAIL_HTTP);
			}
			Log.d("STORYPROTOCOL", context.getString(R.string.market_error_http));
		} catch (JSONException e) {
			if (handler != null) {
				handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_FAIL_JSON);
			}
			Log.d("STORYPROTOCOL", context.getString(R.string.market_error_data));
		} finally {
			if (handler != null) {
				handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_DONE);
			}
			Log.d("STORYPROTOCOL", "Stories downloaded");
		}
	}

	public static void downloadStory(Context context, Handler handler, String storyIdServerside) {
		try {
			HttpClient client = new DefaultHttpClient();
			String url = context.getString(R.string.market_api_story) + storyIdServerside + "/json";
			HttpGet get = new HttpGet(url);
			String content = client.execute(get, new BasicResponseHandler());

			JSONObject storyObject = new JSONObject(content);

			String filename = storyObject.getString(StoryParser.UUID);
			if (!filename.endsWith(".json")) {
				filename = filename.concat(".json");
			}
			FileOutputStream fileOutputStream = context.openFileOutput(filename, Context.MODE_PRIVATE);
			fileOutputStream.write(storyObject.toString().getBytes());
			fileOutputStream.close();

			downloadAssets(context, storyObject, handler);
			Database database = new Database(context);
			database.open();
			database.insertStory(storyObject.getString(StoryParser.UUID), storyObject.getString(StoryParser.AUTHOR),
					storyObject.getString(StoryParser.TITLE), storyObject.getString(StoryParser.AREA),
					storyObject.getString(StoryParser.IMAGE), storyObject.getInt(StoryParser.VERSION));
		} catch (IOException e) {
			handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_FAIL_HTTP);
		} catch (JSONException e) {
			handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_FAIL_JSON);
		} catch (Exception e) {
			handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_FAILED);
		}

		handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_DONE);
	}

	private static void downloadAssets(Context context, JSONObject storyObject, Handler handler) throws JSONException {
		CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
				context,
				"eu-west-1:0524ea09-ca76-4e85-bc4a-a48b98e6b1f4",
				Regions.EU_WEST_1);
		TransferManager transferManager = new TransferManager(cognitoProvider);
		transferManager.getAmazonS3Client().setRegion(Region.getRegion(Regions.EU_WEST_1));

		downloadAsset(context, IMAGES_FOLDER, storyObject.optString(StoryParser.IMAGE, ""), transferManager, handler);

		JSONObject tags = storyObject.getJSONObject(StoryParser.TAGS);
		Iterator<String> keys = tags.keys();
		while (keys.hasNext()) {
			String key = keys.next();
			JSONObject tag = tags.getJSONObject(key);
			if (tag.has(StoryParser.TAG_OPTIONS)) {
				JSONArray options = tag.getJSONArray(StoryParser.TAG_OPTIONS);
				for(int index = 0; index < options.length(); index++) {
					JSONObject option = options.getJSONObject(index);
					if (option.has(StoryParser.HINT_IMAGE_SOURCE)) {
						downloadAsset(context, IMAGES_FOLDER, option.optString(StoryParser.HINT_IMAGE_SOURCE, ""), transferManager, handler);
					}
					if (option.has(StoryParser.HINT_SOUND_SOURCE)) {
						downloadAsset(context, AUDIO_FOLDER, option.optString(StoryParser.HINT_SOUND_SOURCE, ""), transferManager, handler);
					}
				}
			}
		}
	}

	private static boolean downloadAsset(Context context, String serverUrl, String name, TransferManager transferManager, Handler handler) {
		if (StringUtil.isBlank(name)) {
			return false;
		}

		Log.d(LOG, String.format(Locale.ENGLISH, "Downloading %s", name));
		Download download = transferManager.download(BUCKET, serverUrl + name, context.getFileStreamPath(name));
		while (!download.isDone()) {
			Message message = new Message();
			message.what = SimpleStoryHandler.MESSAGE_SHORT_INFO;
			message.arg1 = (int) download.getProgress().getPercentTransferred();
			handler.sendMessage(message);
			try {
				Thread.sleep(250); // don't spam the handler
			} catch (InterruptedException e) {
			}
		}
		Message message = new Message();
		message.what = SimpleStoryHandler.MESSAGE_SHORT_INFO;
		message.arg1 = 100; // downloading is done
		handler.sendMessage(message);

		Log.d(LOG, "Done downloading");
		return true;
	}
}
