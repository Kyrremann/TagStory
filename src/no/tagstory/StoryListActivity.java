package no.tagstory;

import no.tagstory.communication.CommunicationManager;
import no.tagstory.communication.Database;
import no.tagstory.rfid.WriteTag;
import no.tagstory.story.StoryManager;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.TextView;
import android.widget.Toast;

@Deprecated
public class StoryListActivity extends ListActivity {

	private Dialog loginDialog, newUserDialog, aboutDialog, newTagDialog;

	private Context context;
	private Cursor storyCursor;
	private StoryManager storyManager;

	private boolean nfcWriteMode = false;
	private String nfcMessage;
	private WriteTag writeTag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_list);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
		context = this;
		this.storyManager = new StoryManager(this);
		this.storyCursor = storyManager.getStoryList();

		// TODO Make list adapter that works with the image in the database/json
		setListAdapter(new SimpleCursorAdapter(this, R.layout.story_list_item,
				storyCursor, new String[] { Database.STORY_TITLE,
						Database.STORY_AUTHOR }, new int[] {
						R.id.storyListItemTitle, R.id.storyListItemAuthor }));
	}
	
	@Override
	protected void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		
		storyCursor.moveToPosition(position);
				Intent detailIntent = new Intent(this, StoryDetailActivity.class);
		detailIntent.putExtra(Database.STORY_ID, storyCursor.getString(0) + ".json");
		startActivity(detailIntent);
		
	}

	/*@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem item = menu.add(Menu.NONE, 1, Menu.NONE,
				R.string.front_menu_log_in);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(Menu.NONE, 2, Menu.NONE, R.string.front_menu_create_user);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		item = menu.add(Menu.NONE, 6, Menu.NONE, R.string.front_menu_options);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		item = menu.add(Menu.NONE, 5, Menu.NONE,
				R.string.front_menu_update_stories);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		item = menu.add(Menu.NONE, 3, Menu.NONE, R.string.front_menu_about);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		item = menu
				.add(Menu.NONE, 4, Menu.NONE, R.string.front_menu_create_tag);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}*/

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			return false;
		} else if (item.getItemId() == 1) { // Log in
			if (loginDialog == null)
				loginDialog = createLoginDialog();
			loginDialog.show();
			return false;
		} else if (item.getItemId() == 2) { // New user
			if (newUserDialog == null)
				newUserDialog = createNewUserDialog();
			newUserDialog.show();
			return false;
		} else if (item.getItemId() == 3) { // About
			if (aboutDialog == null)
				aboutDialog = createAboutDialog();
			aboutDialog.show();
		} else if (item.getItemId() == 4) { // Create tag
			if (newTagDialog == null)
				newTagDialog = createNewTagDialog();
			newTagDialog.show();
		} else if (item.getItemId() == 5) { // Update
			StoryManager storyManager = new StoryManager(this);
			storyManager.getStoriesFromServer();
		} else if (item.getItemId() == 6) { // Options
		}

		return super.onOptionsItemSelected(item);
	}*/

	@Override
	protected void onNewIntent(Intent intent) {
		if (writeTag != null && nfcWriteMode) {
			writeTag.onNewIntent(intent);
			writeTag.disableForegroundMode();
			writeTag = null;
		} else {
			super.onNewIntent(intent);
		}
	}

	private Dialog createNewTagDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.create_tag_title);
		builder.setCancelable(true);

		View view = View.inflate(this, R.layout.dialog_create_tag, null);
		builder.setView(view);

		final EditText description = (EditText) view
				.findViewById(R.id.create_tag_description);
		final EditText gps = (EditText) view.findViewById(R.id.create_tag_gps);

		builder.setPositiveButton(R.string.create_tag_write,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {

						final Handler handler = new Handler() {

							@Override
							public void handleMessage(Message msg) {
								// TODO: Time to write to tag
								System.out.println(msg.obj);
								nfcMessage = msg.obj.toString();
								nfcWriteMode = true;
								writeTag = new WriteTag(context, msg.obj
										.toString());
								writeTag.enableForegroundMode();
							};
						};

						new Thread(new Runnable() {

							@Override
							public void run() {

								CommunicationManager manager = new CommunicationManager();
								Message msg = Message.obtain();
								msg.obj = manager.postNewTag(description
										.getText().toString(), gps.getText()
										.toString());
								handler.sendMessage(msg);
							}
						}).start();
					}
				});
		builder.setNeutralButton(R.string.cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}

	private Dialog createLoginDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.login_title);
		builder.setCancelable(true);

		final View view = View.inflate(this, R.layout.dialog_login, null);
		builder.setView(view);

		builder.setPositiveButton(R.string.login_login, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Check too see if user is online and sync data, or just
				// let the user log in in offline-mode
				// For now it stores the user in the database if it's a new
				// user, and if the user exists it checks to see if the password
				// is the same
				String username = ((TextView) view
						.findViewById(R.id.login_username)).getText()
						.toString();
				String password = ((TextView) view
						.findViewById(R.id.login_password)).getText()
						.toString();
				Database database = new Database(getApplicationContext());
				database.open();
				if (database.isExistsUser(username)) {
					if (!database.isCorrectPassword(username, password)) {
						Toast.makeText(getApplicationContext(),
								R.string.login_error, Toast.LENGTH_SHORT)
								.show();
						return;
					}
				} else {
					database.setUsernameAndPassword(username, password);
				}

				Toast.makeText(getApplicationContext(), R.string.login_success,
						Toast.LENGTH_SHORT).show();
			}
		});
		builder.setNeutralButton(R.string.login_cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}

	private Dialog createNewUserDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.new_user_title);
		builder.setMessage(R.string.new_user_info);
		builder.setCancelable(true);

		View view = View.inflate(this, R.layout.dialog_new_user, null);
		builder.setView(view);

		builder.setPositiveButton(R.string.new_user_create,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						// TODO Not sure what to do yet
					}
				});
		builder.setNeutralButton(R.string.new_user_cancel,
				new OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.cancel();
					}
				});

		return builder.create();
	}

	private Dialog createAboutDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.dialog_about_title);
		builder.setCancelable(true);
		builder.setMessage(R.string.dialog_about_tagstory);

		builder.setNeutralButton(R.string.dialog_cancel, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}
}
