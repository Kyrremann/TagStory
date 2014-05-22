package no.tagstory.story.activity.option;

import no.tagstory.kines_bursdag.R;
import no.tagstory.story.Story;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.story.StoryPartOption;
import no.tagstory.story.activity.StoryTravelActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.provider.Settings;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

public class ArrowNavigationActivity extends StoryTravelActivity {

	private final float DIST_ACCEPT = 10;
	private final float COMPASS_PUSH = 40;
	private String PLACE;

	private SensorManager mSensorManager;
	private Sensor mSensor;
	private CompassView mView;
	private float dir = 0.0f;
	private float[] compassValues = { 0 };
	private MediaPlayer player;
	private long dist = 100;
	private LocationManager locationManager;
	private LocationListener locationlistener;
	private Location loc;
	private Location lastKnownLocation;
	private String locationProvider;
	private SensorEventListener magnetListener;
	private TextView distance;

	private Bitmap compassCircle;
	private Bitmap compassHand;

	private String previousTag;

	Runnable onEverySecond = null;

	@Override
	protected void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_arrow);

		// getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle
				.getSerializable(StoryTravelActivity.OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);

		((TextView) findViewById(R.id.story_arrow_hint)).setText(option
				.getOptHintText());

		// TODO: Screen orientation
		// setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);

		// Graphic
		Resources res = this.getResources();
		compassCircle = BitmapFactory.decodeResource(res, R.drawable.compass);
		compassHand = BitmapFactory.decodeResource(res, R.drawable.hand);
		mView = new CompassView(this);

		FrameLayout fl = (FrameLayout) findViewById(R.id.story_arrow_layout);
		fl.addView(mView, 0);

		distance = (TextView) findViewById(R.id.story_arrow_distance);

		// TODO: Should we play a sound when you are near enough?
		// player = MediaPlayer.create(this, R.raw.ding);
		// player.setLooping(false);

		mSensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
		mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
		magnetListener = new SensorEventListener() {

			@Override
			public void onSensorChanged(SensorEvent event) {
				compassValues = event.values;
				if (mView != null)
					mView.invalidate();
			}

			@Override
			public void onAccuracyChanged(Sensor sensor, int accuracy) {
				// TODO Auto-generated method stub

			}
		};
		// mSensorManager.registerListener(magnetListener, mSensor,
		// SensorManager.SENSOR_DELAY_NORMAL);

		// Last known user location
		locationManager = (LocationManager) this
				.getSystemService(Context.LOCATION_SERVICE);
		locationlistener = new MyLocationListener();
		locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0,
				0, locationlistener);
		locationManager.requestLocationUpdates(
				LocationManager.NETWORK_PROVIDER, 0, 0, locationlistener);

		if (!locationManager
				.isProviderEnabled(android.location.LocationManager.GPS_PROVIDER)) {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("GPS");
			builder.setMessage(R.string.story_arrow_gps_is_off);
			builder.setPositiveButton(R.string.story_arrow_turn_on,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							Intent myIntent = new Intent(
									Settings.ACTION_LOCATION_SOURCE_SETTINGS);
							startActivity(myIntent);
							dialog.cancel();
						}
					});

			builder.setNegativeButton(R.string.story_arrow_no,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.cancel();
							finish();
						}
					});
			AlertDialog dialog = builder.create();
			dialog.show();
		}
	}

	public void playFoundSound() {
		player.start();
	}

	@Override
	protected void onResume() {
		super.onResume();
		mSensorManager.registerListener(magnetListener, mSensor,
				SensorManager.SENSOR_DELAY_NORMAL);
	}

	@Override
	protected void onStop() {
		mSensorManager.unregisterListener(magnetListener);
		super.onStop();
	}

	/*@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, partTag);
			intent.putExtra(StoryActivity.PREVIOUSTAG, previousTag);
			NavUtils.navigateUpTo(this, intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}*/

	private class CompassView extends View {

		private Paint mPaint;
		private Path arrowPath;

		public CompassView(Context context) {
			super(context);

			mPaint = new Paint();
			mPaint.setAntiAlias(true);
			mPaint.setStyle(Paint.Style.FILL);
			arrowPath = new Path();

			// Construct a wedge-shaped path
			arrowPath.moveTo(0, -100);
			arrowPath.lineTo(-70, 100);
			arrowPath.lineTo(0, 80);
			arrowPath.lineTo(70, 100);
			arrowPath.close();
		}

		@Override
		protected void onDraw(Canvas canvas) {

			int w = canvas.getWidth();
			int h = canvas.getHeight();
			int cx = w / 2;
			int cy = h / 2;

			if (loc == null) {
				distance.setText(R.string.story_arrow_gps_wait);
			} else {
				distance.setText(String.format("%d m",
						Math.max((int) dist - 10, 0)));

				// Draw that compass
				canvas.translate(cx, cy + COMPASS_PUSH);
				canvas.translate(-compassCircle.getWidth() / 2,
						-compassCircle.getHeight() / 2);
				canvas.drawBitmap(compassCircle, 0, 0, mPaint);

				// Back to center!
				canvas.translate(compassCircle.getWidth() / 2,
						compassCircle.getHeight() / 2);
				canvas.rotate(dir - compassValues[0]);
				canvas.translate(-compassHand.getWidth() / 2,
						-compassHand.getHeight() / 2);
				canvas.translate(0, -65);
				canvas.drawBitmap(compassHand, 0, 0, mPaint);
			}
		}

		@Override
		protected void onAttachedToWindow() {
			super.onAttachedToWindow();
		}

		@Override
		protected void onDetachedFromWindow() {
			super.onDetachedFromWindow();
		}
	}

	private class MyLocationListener implements LocationListener {

		@Override
		public void onLocationChanged(Location location) {
			if (location != null) {
				loc = location;
				Location target = new Location("TEST"); // TODO: Set the
														// location here
				target.setLatitude(option.getLatitude());
				target.setLongitude(option.getLongitude());
				dist = (long) loc.distanceTo(target);
				dir = loc.bearingTo(target);
				mView.invalidate();
				// if (dist < DIST_ACCEPT) {
				// playFoundSound();
				// // TODO: Inform the user that they are close enough to find
				// // it
				// }
			}
		}

		@Override
		public void onProviderDisabled(String provider) {
		}

		@Override
		public void onProviderEnabled(String provider) {
		}

		@Override
		public void onStatusChanged(String provider, int status, Bundle extras) {
		}
	}
}
