package no.tagstory.beacons;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.RemoteException;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.Region;
import no.tagstory.R;
import no.tagstory.StoryApplication;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class BeaconTestActivity extends Activity implements BeaconManager.RangingListener, BeaconManager.ServiceReadyCallback {

	private static final String TAG = "BEACONTESTER";
	private static final String ESTIMOTE_PROXIMITY_UUID = "B9407F30-F5F8-466E-AFF9-25556B57FE6D";
	private static final Region ALL_ESTIMOTE_BEACONS = new Region("regionId", ESTIMOTE_PROXIMITY_UUID, null, null);


	private BeaconManager beaconManager;
	private LinearLayout linearLayout;
	private List<String> statistics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_beacon_test);
		linearLayout = (LinearLayout) findViewById(R.id.beacons_layout);
		statistics = new ArrayList<>();

		StoryApplication storyApplication = (StoryApplication) getApplication();
		beaconManager = storyApplication.getBeaconManager(this);
		beaconManager.setRangingListener(this);
	}

	public void saveStatistics(View view) {
		if (view.getId() == R.id.save_statistic) {
			String data = "";
			for (String statistic : statistics) {
				data += statistic + "\n";
			}
			Intent intent = new Intent(android.content.Intent.ACTION_SEND);
			intent.setType("text/plain");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);
			intent.putExtra(Intent.EXTRA_SUBJECT, "Beacon stats");
			intent.putExtra(Intent.EXTRA_TEXT, data);
			startActivity(Intent.createChooser(intent, "Sharing stats with..."));
		}
	}

	@Override
	protected void onStart() {
		super.onStart();
		beaconManager.connect(this);
	}

	@Override
	protected void onStop() {
		super.onStop();
		try {
			beaconManager.stopRanging(ALL_ESTIMOTE_BEACONS);
		} catch (RemoteException e) {
			Log.e(TAG, "Cannot stop but it does not matter now", e);
		}
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		beaconManager.disconnect();
	}

	@Override
	public void onBeaconsDiscovered(Region region, List<Beacon> beacons) {
		Log.d(TAG, "Ranged beacons: " + beacons);
		for (Beacon beacon : beacons) {
			String data = String.format(Locale.ENGLISH, "Major: %d, Minor: %d, Dist: %f",
					beacon.getMajor(), beacon.getMinor(), getDistance(beacon.getRssi(), beacon.getMeasuredPower()));
			statistics.add(data);
			TextView textView = new TextView(this);
			textView.setText(data);
			linearLayout.addView(textView, 0);
		}
		linearLayout.addView(new TextView(this), 0);
	}

	@Override
	public void onServiceReady() {
		try {
			beaconManager.startRanging(ALL_ESTIMOTE_BEACONS);
		} catch (RemoteException e) {
			Log.e(TAG, "Cannot start ranging", e);
		}
	}

	/*
	 * RSSI = TxPower - 10 * n * lg(d)
	 * n = 2 (in free space)
	 *
	 * d = 10 ^ ((TxPower - RSSI) / (10 * n))
	 */
	double getDistance(int rssi, int txPower) {
		return Math.pow(10d, ((double) txPower - rssi) / (10.0 * 2.0));
	}
}
