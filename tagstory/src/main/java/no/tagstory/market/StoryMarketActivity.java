package no.tagstory.market;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.widget.Toast;
import no.tagstory.R;
import no.tagstory.StoryApplication;
import no.tagstory.utils.http.SimpleStoryHandler;
import no.tagstory.utils.http.StoryProtocol;

public class StoryMarketActivity extends FragmentActivity implements SimpleStoryHandler.SimpleCallback {

	private Handler handler;
	private ProgressDialog progressDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setTitle(R.string.market_actionbar_title);

		StoryApplication storyApplication = (StoryApplication) getApplication();

		progressDialog = new ProgressDialog(this);
		progressDialog.setCancelable(false);
		progressDialog.setMessage(getString(R.string.market_info_updating_market));
		progressDialog.show();

		handler = new SimpleStoryHandler(this);
		if (storyApplication.isMarketStoriesEmptyOrOutdated()) {
			downloadNewStoriesForMarket();
		} else {
			handler.sendEmptyMessage(SimpleStoryHandler.MESSAGE_DONE);
		}
	}

	private void downloadNewStoriesForMarket() {
		new Thread(new Runnable() {
			@Override
			public void run() {
				StoryProtocol.downloadNewStoriesToTheStoryApplication(getApplicationContext(), handler);
			}
		}).start();
	}

	private void showMarketFragment() {
		String tag = StoryMarketListFragment.class.getSimpleName();
		Fragment fragment = getSupportFragmentManager().findFragmentByTag(tag);
		if (fragment == null) {
			fragment = new StoryMarketListFragment();
		}

		getSupportFragmentManager().beginTransaction().replace(android.R.id.content, fragment, tag).commit();
	}

	@Override
	public void onMessageDone() {
		progressDialog.cancel();
		showMarketFragment();
	}

	@Override
	public void onMessageInfo(int arg1, int arg2) {
	}

	@Override
	public void onMessageFail(int error) {
		switch (error) {
			case SimpleStoryHandler.MESSAGE_FAIL_HTTP:
				Toast.makeText(this, getString(R.string.market_error_http), Toast.LENGTH_SHORT).show();
				break;
			case SimpleStoryHandler.MESSAGE_FAIL_JSON:
				Toast.makeText(this, getString(R.string.market_error_data), Toast.LENGTH_SHORT).show();
				break;
		}
		progressDialog.cancel();
	}
}
