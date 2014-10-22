package no.tagstory.utils;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;

public class DialogFactory {

	/**
	 * Helper method to create simple dialog with title, message and OK-button
	 *
	 * @param context The application environment
	 * @param title   Title of the dialog
	 * @param message The main message of the dialog
	 * @return a Dialog-object
	 */
	public static Dialog createAboutDialog(Context context, int title, int message) {
		AlertDialog.Builder builder = new AlertDialog.Builder(context);
		builder.setTitle(title);
		builder.setCancelable(true);
		builder.setMessage(message);
		builder.setNeutralButton(android.R.string.ok, new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();
	}
}
