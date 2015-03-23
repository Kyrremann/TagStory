package no.tagstory.adapters;

import android.content.Context;
import android.database.Cursor;
import android.widget.ArrayAdapter;
import no.tagstory.R;

public class SavedTravelsListAdapter extends ArrayAdapter<SavedTravelsListAdapter.Node> {

	public SavedTravelsListAdapter(Context context, Cursor cursor) {
		super(context, R.layout.item_saved_travels, R.id.timestamp);
		generateListBasedOnCursor(cursor);
	}

	private void generateListBasedOnCursor(Cursor cursor) {
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			add(new Node(cursor.getString(0), cursor.getString(3)));
		}
	}

	protected class Node {

		public final String id;
		public final String timestamp;

		public Node(String id, String timestamp) {
			this.id = id;
			this.timestamp = timestamp;
		}

		@Override
		public String toString() {
			return timestamp;
		}
	}
}
