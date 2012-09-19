package no.uio.ifi.inf5261.tagstory;

import java.nio.channels.Selector;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.Database.Database;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

public class StoryListActivity extends FragmentActivity implements
		StoryListFragment.Callbacks {

	private boolean mTwoPane;
	private Dialog loginDialog, newUserDialog, aboutDialog;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_list);

		// getActionBar().setDisplayHomeAsUpEnabled(true);

		if (findViewById(R.id.story_detail_container) != null) {
			mTwoPane = true;
			((StoryListFragment) getSupportFragmentManager().findFragmentById(
					R.id.story_list)).setActivateOnItemClick(true);
		}
	}

	@Override
	public void onItemSelected(String id) {
		if (mTwoPane) {
			Bundle arguments = new Bundle();
			arguments.putString(Database.STORY_ID, id);
			StoryDetailFragment fragment = new StoryDetailFragment();
			fragment.setArguments(arguments);
			getSupportFragmentManager().beginTransaction()
					.replace(R.id.story_detail_container, fragment).commit();

		} else {
			Intent detailIntent = new Intent(this, StoryDetailActivity.class);
			detailIntent.putExtra(Database.STORY_ID, id);
			startActivity(detailIntent);
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem item = menu.add(Menu.NONE, 1, Menu.NONE,
				R.string.front_menu_log_in);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);

		menu.add(Menu.NONE, 2, Menu.NONE, R.string.front_menu_create_user);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_IF_ROOM);
		item = menu.add(R.string.front_menu_options);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		item = menu.add(R.string.front_menu_update_stories);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);
		item = menu.add(Menu.NONE, 3, Menu.NONE, R.string.front_menu_about);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_NEVER);

		return true;
	}

	@Override
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
		}

		return super.onOptionsItemSelected(item);
	}

	private Dialog createLoginDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.login_title);
		builder.setCancelable(true);
		
		View view = View.inflate(this, R.layout.dialog_login, null);
		builder.setView(view);

		builder.setPositiveButton(R.string.login_login, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Check too see if user is online and sync data, or just
				// let the user log in in offline-mode
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

		builder.setPositiveButton(R.string.new_user_create, new OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				// TODO Not sure what to do yet
			}
		});
		builder.setNeutralButton(R.string.new_user_cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}
	

	private Dialog createAboutDialog() {
		AlertDialog.Builder builder = new Builder(this);
		builder.setTitle(R.string.about_title);
		builder.setCancelable(true);
		builder.setMessage(R.string.about_info);
		
		builder.setNeutralButton(R.string.about_cancel, new OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}
}
