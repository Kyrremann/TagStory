package no.tagstory.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import no.tagstory.kines_bursdag.R;

import java.io.InputStream;

public class StoryCursorAdapter extends CursorAdapter {

	private int viewId;
	private LayoutInflater inflater;

	public StoryCursorAdapter(Context context, int textViewResourceId,
	                          Cursor storyCursor) {
		super(context, storyCursor, false);
		this.viewId = textViewResourceId;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		try {
			String imageAsset = cursor.getString(4);
			InputStream inputStream = context.getAssets().open(imageAsset);
			Drawable drawable = Drawable.createFromStream(inputStream, imageAsset);
			((ImageView) view.findViewById(R.id.story_list_image))
					.setImageDrawable(drawable);
		} catch (Exception e) {
			((ImageView) view.findViewById(R.id.story_list_image))
					.setImageResource(R.drawable.placeimg_478_186_arch);
		}
		((TextView) view.findViewById(R.id.storyListItemTitle)).setText(cursor
				.getString(2));
		((TextView) view.findViewById(R.id.storyListItemAuthor))
				.setText("Sted: " + cursor
						.getString(3));
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		final View view = inflater.inflate(viewId, parent, false);
		return view;
	}
}
