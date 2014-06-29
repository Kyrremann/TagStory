package no.tagstory.utils;

import android.content.Context;
import android.content.Intent;
import android.os.Build;

public class ClassVersionFactory {

	public static <T, S> Intent createIntent(Context context, Class<S> honeyCombClass,
	                                         Class<T> normalClass) {
		Intent intent;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			intent = new Intent(context, honeyCombClass);
		} else {
			intent = new Intent(context, normalClass);
		}
		return intent;
	}
}
