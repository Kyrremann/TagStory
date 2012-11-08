package no.uio.ifi.inf5261.tagstory.story.option;

import java.util.ArrayList;
import java.util.List;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryActivity;
import no.uio.ifi.inf5261.tagstory.story.StoryPartOption;
import no.uio.ifi.inf5261.tagstory.story.StoryTravelActivity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.app.NavUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapNavigationActivity extends NFCMapActivity {

	private String previousTag;

	private MapView mapView;
	private List<Overlay> overlayList;
	private MapOverlay mapOverlay;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_story_map);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle
				.getSerializable(StoryTravelActivity.OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);

		if (option.getOptHintText().length() > 0) {
			LinearLayout layout = (LinearLayout) findViewById(R.id.activity_map_layout);
			TextView textView = new TextView(this);
			textView.setText(option.getOptHintText());
			layout.addView(textView);
		}

		mapView = (MapView) findViewById(R.id.story_map_view);
		mapView.setBuiltInZoomControls(true);
		// TODO: Zoom level could be set in the .json file
		mapView.getController().setZoom(15);

		overlayList = mapView.getOverlays();
		mapOverlay = new MapOverlay(getResources()
				.getDrawable(R.drawable.arrow), this);

		GeoPoint geoPoint = new GeoPoint((int) (option.getOptLat() * 1e6),
				(int) (option.getOptLong() * 1e6));
		OverlayItem item = new OverlayItem(geoPoint, "Her skal du starte", "");
		mapOverlay.addOverlay(item);
		mapView.getController().setCenter(geoPoint);

		overlayList.add(mapOverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		return false;
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {

		MenuItem item = menu.add(Menu.NONE, 0, Menu.NONE,
				R.string.story_scan_tag);
		item.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS);

		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, partTag);
			intent.putExtra(StoryActivity.PREVIOUSTAG, previousTag);
			NavUtils.navigateUpTo(this, intent);
			return true;
		} else if (item.getItemId() == 0) {
			startScanning();
		}

		return super.onOptionsItemSelected(item);
	}

	private class MapOverlay extends ItemizedOverlay<OverlayItem> {

		private ArrayList<OverlayItem> mOverlays;
		private Context mContext;

		public MapOverlay(Drawable defaultMarker, Context context) {
			super(boundCenterBottom(defaultMarker));
			mOverlays = new ArrayList<OverlayItem>();
			mContext = context;
		}

		public void addOverlay(OverlayItem item) {
			mOverlays.add(item);
			populate();
		}

		@Override
		protected OverlayItem createItem(int index) {
			return mOverlays.get(index);
		}

		@Override
		public int size() {
			return mOverlays.size();
		}

		@Override
		protected boolean onTap(int index) {
			OverlayItem item = mOverlays.get(index);
			AlertDialog.Builder dialog = new AlertDialog.Builder(mContext);
			dialog.setTitle(item.getTitle());
			dialog.setMessage(item.getSnippet());
			dialog.show();
			return true;
		}
	}


}
