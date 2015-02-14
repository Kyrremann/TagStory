package no.tagstory.utils;

import android.app.Dialog;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;

public class GooglePlayServiceUtils {

	private final static String TAG = "Location Updates";

	public final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;

	public static boolean servicesConnected(FragmentActivity activity) {
		int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

		if (ConnectionResult.SUCCESS == resultCode) {
			Log.d(TAG, "Google Play services is available.");
			return true;
		} else {
			Log.d(TAG, "Google Play services is uavailable.");
			Dialog errorDialog = GooglePlayServicesUtil.getErrorDialog(
					resultCode, activity, CONNECTION_FAILURE_RESOLUTION_REQUEST);

			if (errorDialog != null) {
				ErrorDialogFragment errorFragment = new ErrorDialogFragment();
				errorFragment.setDialog(errorDialog);
				errorFragment.show(activity.getSupportFragmentManager(), TAG);
				return false;
			}
		}

		return false;
	}

	public static class ErrorDialogFragment extends DialogFragment {

		private Dialog mDialog;

		public ErrorDialogFragment() {
			super();
			mDialog = null;
		}

		public void setDialog(Dialog dialog) {
			mDialog = dialog;
		}

		@Override
		public Dialog onCreateDialog(Bundle savedInstanceState) {
			return mDialog;
		}
	}
}
