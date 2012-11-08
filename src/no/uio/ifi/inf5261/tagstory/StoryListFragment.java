package no.uio.ifi.inf5261.tagstory;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.Database.Database;
import no.uio.ifi.inf5261.tagstory.story.StoryManager;
import android.app.Activity;
import android.content.Context;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.view.View;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;

public class StoryListFragment extends ListFragment {

	private static final String STATE_ACTIVATED_POSITION = "activated_position";

	private Callbacks mCallbacks = sListCallbacks;
	private int mActivatedPosition = ListView.INVALID_POSITION;
	private StoryManager storyManager;
	private Context context;
	private Cursor storyCursor;

	public interface Callbacks {

		public void onItemSelected(String id);
	}

	private static Callbacks sListCallbacks = new Callbacks() {
		@Override
		public void onItemSelected(String id) {
		}
	};

	public StoryListFragment() {
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		this.context = getActivity();
		this.storyManager = new StoryManager(context);
		this.storyCursor = storyManager.getStoryList();
		
		setListAdapter(new SimpleCursorAdapter(
				context,
				R.layout.story_list_item,
				storyCursor,
				new String[] { Database.STORY_TITLE, Database.STORY_AUTHOR },
				new int[] { R.id.storyListItemTitle, R.id.storyListItemAuthor },
				0));
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		if (savedInstanceState != null
				&& savedInstanceState.containsKey(STATE_ACTIVATED_POSITION)) {
			setActivatedPosition(savedInstanceState
					.getInt(STATE_ACTIVATED_POSITION));
		}
	}

	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		if (!(activity instanceof Callbacks)) {
			throw new IllegalStateException(
					"Activity must implement fragment's callbacks.");
		}

		mCallbacks = (Callbacks) activity;
	}

	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = sListCallbacks;
	}

	@Override
	public void onListItemClick(ListView listView, View view, int position,
			long id) {
		super.onListItemClick(listView, view, position, id);
		storyCursor.moveToPosition(position);
		// mCallbacks.onItemSelected(storyCursor.getString(0));
		mCallbacks.onItemSelected(storyCursor.getString(0) + ".json");
	}

	@Override
	public void onSaveInstanceState(Bundle outState) {
		super.onSaveInstanceState(outState);
		if (mActivatedPosition != ListView.INVALID_POSITION) {
			outState.putInt(STATE_ACTIVATED_POSITION, mActivatedPosition);
		}
	}

	public void setActivateOnItemClick(boolean activateOnItemClick) {
		getListView().setChoiceMode(
				activateOnItemClick ? ListView.CHOICE_MODE_SINGLE
						: ListView.CHOICE_MODE_NONE);
	}

	public void setActivatedPosition(int position) {
		if (position == ListView.INVALID_POSITION) {
			getListView().setItemChecked(mActivatedPosition, false);
		} else {
			getListView().setItemChecked(position, true);
		}

		mActivatedPosition = position;
	}
}
