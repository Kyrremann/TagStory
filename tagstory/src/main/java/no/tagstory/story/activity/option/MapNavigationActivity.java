package no.tagstory.story.activity.option;

import android.os.Bundle;
import com.google.android.gms.maps.*;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import no.tagstory.R;
import no.tagstory.story.activity.StoryTravelActivity;

public class MapNavigationActivity extends StoryTravelActivity {

	public static final String ZOOM = "zoom";
	public static final String LONGITUDE = "longitude";
	public static final String LATITUDE = "latitude";

	private SupportMapFragment mapView;
	private GoogleMap googleMap;
	private CameraPosition lastCameraPosition;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_map);

		mapView = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.story_map_view);

		googleMap = mapView.getMap();
		googleMap.setMyLocationEnabled(true);
		UiSettings uiSettings = googleMap.getUiSettings();
		uiSettings.setMyLocationButtonEnabled(true);
		uiSettings.setZoomControlsEnabled(true);

		if (savedInstanceState != null) {
			float zoom = savedInstanceState.getFloat(ZOOM);
			double latitude = savedInstanceState.getDouble(LATITUDE);
			double longitue = savedInstanceState.getDouble(LONGITUDE);
			LatLng latLng = new LatLng(latitude, longitue);
			lastCameraPosition = CameraPosition.fromLatLngZoom(latLng, zoom);
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		LatLng latLng = new LatLng(option.getLatitude(), option.getLongitude());
		CameraUpdate cameraUpdate;

		if (lastCameraPosition == null) {
			cameraUpdate = CameraUpdateFactory.newLatLngZoom(latLng, option.getZoomLevel());
		} else {
			cameraUpdate = CameraUpdateFactory.newCameraPosition(lastCameraPosition);
		}
		googleMap.moveCamera(cameraUpdate);

		addMarker(googleMap, latLng, R.string.dummy_map_target, -1);
	}

	// TODO Implement marker snippet in the story json
	private void addMarker(GoogleMap map, LatLng latLng, int title, int snippetId) {
		String snippet = "";
		if (snippetId == -1) {
			snippet = getString(R.string.dummy_map_snippet);
		} else {
			snippet = getString(snippetId);
		}
		map.addMarker(
				new MarkerOptions()
						.position(latLng)
						.title(getString(title))
						.snippet(snippet));
	}

	@Override
	protected void onSaveInstanceState(Bundle outState) {
		CameraPosition cameraPosition = googleMap.getCameraPosition();
		LatLng cameraTarget = cameraPosition.target;
		outState.putFloat(ZOOM, cameraPosition.zoom);
		outState.putDouble(LATITUDE, cameraTarget.latitude);
		outState.putDouble(LONGITUDE, cameraTarget.longitude);
	}
}
