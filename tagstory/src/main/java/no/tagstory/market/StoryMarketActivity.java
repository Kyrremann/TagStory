package no.tagstory.market;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.BasicResponseHandler;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;

public class StoryMarketActivity extends FragmentActivity {

	private static final int MESSAGE_DONE = 0;
	private static final int MESSAGE_FAIL_JSON = -1;
	private static final int MESSAGE_FAIL_HTTP = -2;

	private Handler handler;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.market_actionbar_title);

		StoryApplication storyApplication = (StoryApplication) getApplication();

		final ProgressDialog progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.market_info_updating_market));
		progressDialog.show();

		handler = createHandler(progressDialog);
		if (storyApplication.isMarkedStoriesEmptyOrOutdated()) {
			downloadNewStoriesForMarked();
		} else {
			handler.sendEmptyMessage(MESSAGE_DONE);
		}
	}

	private void downloadNewStoriesForMarked() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				try {
					HttpClient client = new DefaultHttpClient();
					HttpGet get = new HttpGet(getString(R.string.market_api_stories));
					String content = client.execute(get, new BasicResponseHandler());

					((StoryApplication) getApplication()).setMarkedstories(new JSONArray(content));
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
	}

	private Handler createHandler(final ProgressDialog progressDialog) {
		return new Handler(new Handler.Callback() {
			@Override
			public boolean handleMessage(Message msg) {
				switch (msg.what) {
					case MESSAGE_DONE:
						progressDialog.cancel();
						showMarkedFragment();
						break;
					case MESSAGE_FAIL_HTTP:
						Toast.makeText(getApplicationContext(), getString(R.string.market_error_http), Toast.LENGTH_SHORT).show();
						break;
					case MESSAGE_FAIL_JSON:
						Toast.makeText(getApplicationContext(), getString(R.string.market_error_data), Toast.LENGTH_SHORT).show();
						break;
					default:
						break;
				}

				return true;
			}
		});
	}

	private void showMarkedFragment() {
		String tag = StoryMarketListFragment.class.getSimpleName();
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
		if (fragment == null) {
			fragment = new StoryMarketListFragment();
		}

		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, tag).commit();
	}
}
