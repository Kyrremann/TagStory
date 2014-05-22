package no.tagstory;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

import no.tagstory.communication.Database;
import no.tagstory.communication.ServerCommunication;
import no.tagstory.kines_bursdag.R;
import no.tagstory.story.StoryManager;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.DialogInterface.OnClickListener;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ListView;

public class TagStoryActivity extends FragmentActivity {

	protected Dialog tagstoryDialog, klimaDialog;

	protected Cursor storyCursor;
	protected StoryManager storyManager;
	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
	protected ListView listView;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_list);
		// setContentView(R.layout.activity_story_hevstemmen);

		storyManager = new StoryManager(this);
		storyCursor = storyManager.getStoryList();

		// TODO Make list adapter that works with the image in the database/json
		initializeListView();
		servicesConnected();
		listView.performItemClick(null, 0, -1);
	}

	protected void initializeListView() {
		listView = (ListView) findViewById(R.id.story_list);
		listView.setAdapter(new StoryAdapter(this, R.layout.story_list_item,
				storyCursor));
		listView.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> l, View view, int position,
					long id) {
				storyCursor.moveToPosition(position);
				Intent detailIntent = new Intent(TagStoryActivity.this,
						StoryDetailActivity.class);
				detailIntent.putExtra(Database.STORY_ID,
						storyCursor.getString(0) + ".json");
				startActivity(detailIntent);

			}
		});
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		// Decide what to do based on the original request code
		switch (requestCode) {
		case CONNECTION_FAILURE_RESOLUTION_REQUEST:
			/*
			 * If the result code is Activity.RESULT_OK, try
			 * to connect again
			 */
			switch (resultCode) {
			case Activity.RESULT_OK:
				/*
				 * Try the request again
				 */
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
		// TODO Add menu interaction
		int id = item.getItemId();
		if (id == R.id.menu_about) {
			if (tagstoryDialog == null) {
				tagstoryDialog = createInfoDialog(R.string.dialog_about_title,
						R.string.dialog_about_tagstory);
			}
			tagstoryDialog.show();
		}
//		else if (id == R.id.menu_klimanettverk) {
//			if (klimaDialog == null) {
//				klimaDialog = createInfoDialog(R.string.dialog_about_title,
//						R.string.dialog_about_klima);
//			}
//			klimaDialog.show();
//		}
		// else if (id == R.id.menu_gps) {
		// // startActivity(new Intent(this, LocationTester.class));
		// ServerCommunication.sendTempStatistic(
		// "" + System.currentTimeMillis(), "1337");
		// }

		return super.onMenuItemSelected(featureId, item);
	}

	private Dialog createInfoDialog(int title, int info) {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(title);
		builder.setCancelable(true);
		builder.setMessage(info);

		builder.setNeutralButton(android.R.string.ok, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}

	private boolean servicesConnected() {
		// Check that Google Play services is available
		int resultCode = GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(this);
		// If Google Play services is available
		if (ConnectionResult.SUCCESS == resultCode) {
			// In debug mode, log the status
			Log.d("Location Updates", "Google Play services is available.");
			// Continue
			return true;
			// Google Play services was not available for some reason
		} else {
			Log.d("Location Updates", "Google Play services is uavailable.");
			// Get the error code
			int errorCode = resultCode; // connectionResult.getErrorCode();
			// Get the error dialog from Google Play services
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					errorCode, this, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			// If Google Play services can provide an error dialog
			if (errorDialog != null) {
				// Create a new DialogFragment for the error dialog
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				// Set the dialog in the DialogFragment
				errorFragment.setDialog(errorDialog);
				// Show the error dialog in the DialogFragment
				errorFragment.show(getSupportFragmentManager(),
						"Location Updates");
				return false;
			}
		}
		return false;
	}

	// Define a DialogFragment that displays the error dialog
	public static class ErrorDialogFragment extends DialogFragment {
		// Global field to contain the error dialog
		private Dialog mDialog;

		// Default constructor. Sets the dialog field to null
		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		// Set the dialog to display
		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		// Return a Dialog to the DialogFragment.
		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
}
