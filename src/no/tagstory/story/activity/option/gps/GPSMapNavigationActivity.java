package no.tagstory.story.activity.option.gps;

import no.tagstory.hev_stemmen.R;
import no.tagstory.story.Story;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.story.StoryPartOption;
import no.tagstory.story.activity.StoryTravelActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class GPSMapNavigationActivity extends GPSActivity {

	private SupportMapFragment mapView;

	@Override
	protected void onCreate(Bundle arg0) {
		super.onCreate(arg0);
		setContentView(R.layout.activity_story_map);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
/*
		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle
				.getSerializable(StoryTravelActivity.OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);*/

		if (option.getOptHintText().length() > 0) {
			hintText = (TextView) findViewById(R.id.story_option_hint);
			hintText.setText(option.getOptHintText());
			hintText.setVisibility(View.VISIBLE);
		}
		
		mapView = (SupportMapFragment) getSupportFragmentManager()
				.findFragmentById(R.id.story_map_view);
		GoogleMap googleMap = mapView.getMap();
		googleMap.getUiSettings().setMyLocationButtonEnabled(true);
		// TODO: Zoom level could be set in the .json file
		googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(
				option.getLatitude(), option.getLongitude()), 15.0F));
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
