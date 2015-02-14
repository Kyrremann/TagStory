package no.tagstory.story.activity.option;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.UiSettings;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import no.tagstory.R;
import no.tagstory.story.activity.StoryTravelActivity;

public class MapNavigationActivity extends StoryTravelActivity {

	private SupportMapFragment mapView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_story_map);
	}

	@Override
	protected void onResume() {
		super.onResume();

		mapView = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.story_map_view);
		GoogleMap googleMap = mapView.getMap();
		UiSettings uiSettings = googleMap.getUiSettings();
		uiSettings.setMyLocationButtonEnabled(true);
		uiSettings.setZoomControlsEnabled(true);

		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				option.getLatitude(), option.getLongitude()), option.getZoomLevel()));
		googleMap.setMyLocationEnabled(true);

		addMarker(googleMap, option.getLatitude(), option.getLongitude(), R.string.map_target, -1);
	}

	// TODO Implement marker snippet in the story json
	private void addMarker(GoogleMap map, double lat, double lon, int title, int snippetId) {
		String snippet = "";
		if (snippetId == -1) {
			snippet = getString(R.string.map_default_snippet);
		} else {
			snippet = getString(snippetId);
		}
		map.addMarker(new MarkerOptions().position(new LatLng(lat, lon)).title(getString(title)).snippet(snippet));
	}
}
