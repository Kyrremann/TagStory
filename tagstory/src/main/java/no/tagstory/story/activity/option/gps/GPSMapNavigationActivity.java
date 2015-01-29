package no.tagstory.story.activity.option.gps;

import android.os.Bundle;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import no.tagstory.R;

public class GPSMapNavigationActivity extends GPSActivity {

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
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				option.getLatitude(), option.getLongitude()), option.getZoomLevel()));
		googleMap.setMyLocationEnabled(true);

		addMarker(googleMap, option.getLatitude(), option.getLongitude(),
				R.string.map_target, -1);
	}

	private void addMarker(GoogleMap map, double lat, double lon, int title,
	                       int snippetId) {
		String snippet = "";
		if (snippetId == -1) {
			snippet = getString(R.string.map_default_snippet);
		} else {
			snippet = getString(snippetId);
		}
		map.addMarker(new MarkerOptions().position(new LatLng(lat, lon))
				.title(getString(title)).snippet(snippet));
	}
}
