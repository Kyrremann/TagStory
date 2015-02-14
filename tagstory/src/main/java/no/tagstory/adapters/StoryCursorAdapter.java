package no.tagstory.adapters;

import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import no.tagstory.R;
import no.tagstory.utils.ImageLoaderUtils;

import java.io.FileNotFoundException;

public class StoryCursorAdapter extends CursorAdapter {

	private int viewId;
	private LayoutInflater inflater;
	private ImageLoadingListener animateFirstListener = new ImageLoaderUtils.AnimateFirstDisplayListener();

	public StoryCursorAdapter(Context context, int textViewResourceId, Cursor storyCursor) {
		super(context, storyCursor, false);
		this.viewId = textViewResourceId;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public void bindView(View view, Context context, Cursor cursor) {
		((TextView) view.findViewById(R.id.title)).setText(cursor.getString(2));
		try {
			Bitmap myBitmap = BitmapFactory.decodeStream(context.openFileInput(cursor.getString(4)));
			((ImageView) view.findViewById(R.id.image))
					.setImageBitmap(myBitmap);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
//		ImageLoader.getInstance().displayImage(imagePath(), (ImageView) view.findViewById(R.id.image), ImageLoaderUtils.options, animateFirstListener);
	}

	@Override
	public View newView(Context context, Cursor cursor, ViewGroup parent) {
		return inflater.inflate(viewId, parent, false);
	}
}
