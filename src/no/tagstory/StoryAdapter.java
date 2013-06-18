package no.tagstory;

import java.io.InputStream;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class StoryAdapter extends CursorAdapter {

	private int viewId;
	private Cursor storyCursor;
	private Context context;
	private LayoutInflater inflater;

	public StoryAdapter(Context context, int textViewResourceId,
			Cursor storyCursor) {
		super(context, storyCursor, false);
		this.viewId = textViewResourceId;
		this.storyCursor = storyCursor;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		try {
			InputStream inputStream = context.getAssets().open(
					cursor.getString(3));
			Drawable image = Drawable.createFromStream(inputStream,
					cursor.getString(3));
			((ImageView) view.findViewById(R.id.story_list_image))
					.setImageDrawable(image);
		} catch (Exception e) {
			((ImageView) view.findViewById(R.id.story_list_image))
					.setImageResource(R.drawable.flytebrygge);
		}
		((TextView) view.findViewById(R.id.storyListItemTitle)).setText(cursor
				.getString(2));
		((TextView) view.findViewById(R.id.storyListItemAuthor))
				.setText("Sted: <put here>");
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = inflater.inflate(viewId, parent, false);
		return view;
	}

	// @Override
	// public View getView(int position, View convertView, ViewGroup parent) {
	// ViewHolder holder;
	//
	// if (convertView == null) {
	// holder = new ViewHolder();
	// convertView = View.inflate(context, viewId, null);
	// holder.title = (TextView) convertView
	// .findViewById(R.id.storyListItemTitle);
	// convertView.setTag(holder);
	// } else {
	// holder = (ViewHolder) convertView.getTag();
	// }
	//
	// storyCursor.move(position);
	// holder.title.setText(storyCursor.getString(2));
	// System.out.println("okay");
	//
	// // TODO Auto-generated method stub
	// return convertView;
	// }
	//
	// private class ViewHolder {
	// TextView title;
	// TextView author;
	// TextView length;
	// ImageView image;
	// // TextView rating;
	// }
}
