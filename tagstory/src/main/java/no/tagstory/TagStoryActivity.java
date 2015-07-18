package no.tagstory;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
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
import no.tagstory.profile.StatisticsActivity;
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
	public boolean onMenuItemSelected(int featureId, MenuItem item) {
		if (super.onMenuItemSelected(featureId, item)) {
			return false;
		}
		switch (item.getItemId()) {
			case R.id.menu_about:
				showAboutTagStoryDialog();
				break;
			case R.id.menu_story_market:
				startMarketActivity();
				break;
			case R.id.menu_statistics:
				startStatisticsActivity();
				break;
		}

		return true;
	}

	private void startStatisticsActivity() {
		startActivity(new Intent(this, StatisticsActivity.class));
	}

	protected void startMarketActivity() {
		startActivity(new Intent(this, StoryMarketActivity.class));
	}

	private void showAboutTagStoryDialog() {
		if (aboutTagStoryDialog == null) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
			builder.setTitle(R.string.dialog_about_title);
			builder.setMessage(R.string.dialog_about_tagstory);
			builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					dialog.cancel();
				}
			});
			builder.setPositiveButton(R.string.dialog_about_button_contact, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialog, int which) {
					Intent intent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
							"mailto", getString(R.string.contact_tagstory), null));
					intent.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.dummy_email_subject));
					startActivity(Intent.createChooser(intent, getString(R.string.dialog_chooser_email)));
					dialog.cancel();
				}
			});

			aboutTagStoryDialog = builder.create();
		}
		aboutTagStoryDialog.show();
	}

	@Override
	public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
		storyCursor.moveToPosition(position);
		Intent detailIntent = ClassVersionFactory.createIntent(getApplicationContext(),
				StoryDetailActivityHoneycomb.class, StoryDetailActivity.class);
		detailIntent.putExtra(StoryParser.UUID, storyCursor.getString(0));
		startActivity(detailIntent);
	}

	public void onClick(View v) {
		switch (v.getId()) {
			case R.id.visit_market:
				startMarketActivity();
				break;
		}
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		super.onConnected(connectionHint);
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}
}
