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
import no.tagstory.utils.Tuple;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class SavedTravelsListAdapter extends ArrayAdapter<Tuple> {

	private final Context context;
	private SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd.MM.yyyy HH:mm");

	public SavedTravelsListAdapter(Context context, List<Tuple> tuples) {
		super(context, R.layout.item_saved_travels, R.id.timestamp);
		this.context = context;
		for (Tuple tuple : tuples) {
			add(tuple);
		}
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

		String timestamp = formatTimestamp(String.valueOf(getItem(position).o2));
		holder.timestamp.setText(timestamp);
		holder.delete.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				Tuple tuple = getItem(position);
				Database database = new Database(context);
				database.open();
				database.deleteSavedTravel(String.valueOf(tuple.o1));
				database.close();
				remove(tuple);
			}
		});

		return convertView;
	}

	private String formatTimestamp(String timestamp) {
		Date date;
		try {
			date = DateUtils.parseSqliteDate(timestamp);
		} catch (ParseException e) {
			return timestamp;
		}
		return simpleDateFormat.format(date);
	}

	private class ViewHolder {
		TextView timestamp;
		TextView delete;
	}
}
