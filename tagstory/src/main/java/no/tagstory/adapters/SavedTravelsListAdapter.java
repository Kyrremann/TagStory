package no.tagstory.adapters;

import android.content.Context;
import android.database.Cursor;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;
import no.tagstory.R;
import no.tagstory.utils.Database;
import no.tagstory.utils.DateUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class SavedTravelsListAdapter extends ArrayAdapter<SavedTravelsListAdapter.Node> {

	private final Context context;

	public SavedTravelsListAdapter(Context context, Cursor cursor) {
		super(context, R.layout.item_saved_travels, R.id.timestamp);
		this.context = context;
		generateListBasedOnCursor(cursor);
	}

	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder holder;

		if (convertView == null) {
			holder = new ViewHolder();
			convertView = View.inflate(context, R.layout.item_saved_travels, null);
			holder.timestamp = (TextView) convertView.findViewById(R.id.timestamp);
			holder.delete = (TextView) convertView.findViewById(R.id.delete);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		holder.timestamp.setText(formatTimestamp(getItem(position).timestamp));
		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Node node = getItem(position);
				Database database = new Database(context);
				database.open();
				database.deleteSavedTravel(node.id);
				database.close();
				remove(node);
			}
		});

		return super.getView(position, convertView, parent);
	}

	private String formatTimestamp(String timestamp) {
		Date date;
		try {
			date = DateUtils.parseSqliteDate(timestamp);
		} catch (ParseException e) {
			e.printStackTrace();
			return timestamp;
		}
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:MM");
		return simpleDateFormat.format(date);
	}

	private class ViewHolder {
		TextView timestamp;
		TextView delete;
	}

	private void generateListBasedOnCursor(Cursor cursor) {
		cursor.moveToFirst();
		while (!cursor.isAfterLast()) {
			add(new Node(cursor.getString(0), cursor.getString(3)));
			cursor.moveToNext();
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
