package no.tagstory;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.content.IntentSender;
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
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.listener.PauseOnScrollListener;
import no.tagstory.adapters.StoryCursorAdapter;
import no.tagstory.honeycomb.StoryDetailActivityHoneycomb;
import no.tagstory.marked.StoryMarkedActivity;
import no.tagstory.story.StoryManager;
import no.tagstory.utils.*;

import static no.tagstory.utils.GooglePlayServiceUtils.CONNECTION_FAILURE_RESOLUTION_REQUEST;

public class TagStoryActivity extends FragmentActivity implements OnItemClickListener, GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

	protected Dialog aboutTagStoryDialog;
	protected Cursor storyCursor;
	protected StoryManager storyManager;
	protected ListView listView;

	private boolean pauseOnScroll = false;
	private boolean pauseOnFling = true;

	/* Track whether the sign-in button has been clicked so that we know to resolve
	* all issues preventing sign-in without waiting.
	*/
	private boolean mSignInClicked;

	/* Store the connection result from onConnectionFailed callbacks so that we can
	 * resolve them when the user clicks sign-in.
	 */
	private ConnectionResult mConnectionResult;

	/* Request code used to invoke sign in user interactions. */
	private static final int RC_SIGN_IN = 0;

	/* Client used to interact with Google APIs. */
	private GoogleApiClient mGoogleApiClient;

	/* A flag indicating that a PendingIntent is in progress and prevents
	   * us from starting further intents.
	   */
	private boolean mIntentInProgress;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		GooglePlayServiceUtils.servicesConnected(this);

		mGoogleApiClient = new GoogleApiClient.Builder(this)
				.addConnectionCallbacks(this)
				.addOnConnectionFailedListener(this)
				.addApi(Plus.API)
				.addScope(Plus.SCOPE_PLUS_LOGIN)
				.build();
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
			case RC_SIGN_IN:
				if (resultCode != RESULT_OK) {
					mSignInClicked = false;
				}

				mIntentInProgress = false;

				if (!mGoogleApiClient.isConnecting()) {
					mGoogleApiClient.connect();
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
		switch (item.getItemId()) {
			case R.id.menu_about:
				showAboutTagStoryDialog();
				break;
			case R.id.menu_story_marked:
				startMarkedActivity();
				break;
			case R.id.menu_sign_in:
				if (!mGoogleApiClient.isConnecting()) {
					mSignInClicked = true;
					resolveSignInError();
				}
				break;
		}

		return true;
	}

	protected void startMarkedActivity() {
		startActivity(new Intent(this, StoryMarkedActivity.class));
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
	protected void onStart() {
		super.onStart();
		mGoogleApiClient.connect();
	}

	@Override
	protected void onStop() {
		super.onStop();

		if (mGoogleApiClient.isConnected()) {
			mGoogleApiClient.disconnect();
		}
	}

	public void onConnectionSuspended(int cause) {
		mGoogleApiClient.connect();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		mSignInClicked = false;
		Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();
	}

	/* A helper method to resolve the current ConnectionResult error. */
	private void resolveSignInError() {
		if (mConnectionResult.hasResolution()) {
			try {
				mIntentInProgress = true;
				startIntentSenderForResult(mConnectionResult.getResolution().getIntentSender(),
						RC_SIGN_IN, null, 0, 0, 0);
			} catch (IntentSender.SendIntentException e) {
				// The intent was canceled before it was sent.  Return to the default
				// state and attempt to connect to get an updated ConnectionResult.
				e.printStackTrace();
				mIntentInProgress = false;
				mGoogleApiClient.connect();
			}
		}
	}

	public void onConnectionFailed(ConnectionResult result) {
//		if (!mIntentInProgress && result.hasResolution()) {
//			try {
//				mIntentInProgress = true;
//				startIntentSenderForResult(result.getResolution().getIntentSender(),
//						RC_SIGN_IN, null, 0, 0, 0);
//			} catch (IntentSender.SendIntentException e) {
//				// The intent was canceled before it was sent.  Return to the default
//				// state and attempt to connect to get an updated ConnectionResult.
//				mIntentInProgress = false;
//				mGoogleApiClient.connect();
//			}
//		}
		if (!mIntentInProgress) {
			// Store the ConnectionResult so that we can use it later when the user clicks
			// 'sign-in'.
			mConnectionResult = result;

			if (mSignInClicked) {
				// The user has already clicked 'sign-in' so we attempt to resolve all
				// errors until the user is signed in, or they cancel.
				resolveSignInError();
			}
		}
	}
}
