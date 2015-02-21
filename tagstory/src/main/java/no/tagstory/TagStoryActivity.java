package no.tagstory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;
import android.widget.Toast;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import no.tagstory.adapters.StoryCursorAdapter;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.market.StoryMarketActivity;
import no.tagstory.profile.ProfileActivity;
import no.tagstory.story.StoryManager;
import no.tagstory.utils.*;

import static no.tagstory.utils.GooglePlayServiceUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST;

public class TagStoryActivity extends LoginFragmentActivity implements OnItemClickListener {

	protected Dialog aboutTagStoryDialog;
	protected Cursor storyCursor;
	protected StoryManager storyManager;
	protected ListView listView;

	private boolean pauseOnScroll = false;
	private boolean pauseOnFling = true;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GooglePlayServiceUtils.servicesConnected(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		storyManager = new StoryManager(this);
		storyCursor = storyManager.getCursorOverStories();
		if (storyCursor.getCount() > 0) {
			initializeListView();
		} else {
			initializeNoStoriesView();
		}
	}

	private void applyScrollListener() {
		listView.setOnScrollListener(new PauseOnScrollListener(ImageLoader.getInstance(), pauseOnScroll, pauseOnFling));
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		ImageLoaderUtils.AnimateFirstDisplayListener.displayedImages.clear();
	}

	private void initializeNoStoriesView() {
		setContentView(R.layout.activity_no_stories);
	}

	protected void initializeListView() {
		setContentView(R.layout.activity_story_list);
		listView = (ListView) findViewById(R.id.story_list);
		listView.setAdapter(new StoryCursorAdapter(this, R.layout.item_story_list, storyCursor));
		listView.setOnItemClickListener(this);
		applyScrollListener();
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		switch (requestCode) {
			case CONNECTION_FAILURE_RESOLUTION_REQUEST:
				switch (resultCode) {
					case Activity.RESULT_OK:
						// Try the request again
						break;
				}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.main_menu, menu);
		return true;
	}

	@Override
	public boolean onPrepareOptionsMenu(Menu menu) {
		if (mGoogleApiClient.isConnected()) {
			MenuItem profile = menu.findItem(R.id.menu_profile);
			if (profile != null) {
				profile.setEnabled(true);
			}
		}
		return super.onPrepareOptionsMenu(menu);
	}

	@Override
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (super.onMenuItemSelected(featureId, item)) {
			return false;
		}
		switch (item.getItemId()) {
			case R.id.menu_about:
				showAboutTagStoryDialog();
				break;
			case R.id.menu_story_marked:
				startMarkedActivity();
				break;
			case R.id.menu_profile:
				startProfileActivity();
				break;
		}

		return true;
	}

	private void startProfileActivity() {
		startActivity(new Intent(this, ProfileActivity.class));
	}

	protected void startMarkedActivity() {
		startActivity(new Intent(this, StoryMarketActivity.class));
	}

	private void showAboutTagStoryDialog() {
		if (aboutTagStoryDialog == null) {
			aboutTagStoryDialog = DialogFactory.createAboutDialog(this, R.string.dialog_about_title,
					R.string.dialog_about_tagstory);
		}
		aboutTagStoryDialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		storyCursor.moveToPosition(position);
		Intent detailIntent = ClassVersionFactory.createIntent(getApplicationContext(),
				StoryDetailActivityHoneycomb.class, StoryDetailActivity.class);
		detailIntent.putExtra(Database.STORY_ID,
				storyCursor.getString(0));
		startActivity(detailIntent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.visit_marked:
				startMarkedActivity();
				break;
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		super.onConnected(connectionHint);
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}
}
