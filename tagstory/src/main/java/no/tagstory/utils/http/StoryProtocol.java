package no.tagstory.utils.http;

import android.content.Context;
import android.os.Handler;
import android.util.Log;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class StoryProtocol {

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
}
