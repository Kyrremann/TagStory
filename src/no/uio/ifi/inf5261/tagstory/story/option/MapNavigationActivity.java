package no.uio.ifi.inf5261.tagstory.story.option;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

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
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.android.maps.GeoPoint;
import com.google.android.maps.ItemizedOverlay;
import com.google.android.maps.MapActivity;
import com.google.android.maps.MapView;
import com.google.android.maps.Overlay;
import com.google.android.maps.OverlayItem;

public class MapNavigationActivity extends MapActivity {

	private Story story;
	private StoryPartOption option;
	private String partTag, previousTag;

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

		((TextView) findViewById(R.id.story_map_hint)).setText(option
				.getOptHintText());

		mapView = (MapView) findViewById(R.id.story_map_view);
		mapView.setBuiltInZoomControls(true);

//		overlayList = mapView.getOverlays();
//		mapOverlay = new MapOverlay(getResources().getDrawable(
//				R.drawable.ic_launcher), this);
//
//		GeoPoint geoPoint = new GeoPoint((int) (59.908803 * 1e6),
//				(int) (10.776598 * 1e6));
//		OverlayItem item = new OverlayItem(geoPoint, "Go here",
//				"Outside the prison");
//		mapOverlay.addOverlay(item);
//
//		GeoPoint point2 = new GeoPoint(35410000, 139460000);
//		OverlayItem overlayitem2 = new OverlayItem(point2, "Sekai, konichiwa!",
//				"I'm in Japan!");
//		mapOverlay.addOverlay(overlayitem2);
//
//		GeoPoint point = new GeoPoint(19240000, -99120000);
//		OverlayItem overlayitem = new OverlayItem(point, "Hola, Mundo!",
//				"I'm in Mexico City!");
//		mapOverlay.addOverlay(overlayitem);

		overlayList.add(mapOverlay);
	}

	@Override
	protected boolean isRouteDisplayed() {
		// TODO Auto-generated method stub
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
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, option.getOptNext());
			intent.putExtra(StoryActivity.PREVIOUSTAG, partTag);
			startActivity(intent);
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
