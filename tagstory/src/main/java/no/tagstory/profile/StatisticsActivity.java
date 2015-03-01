package no.tagstory.profile;

import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.widget.CursorAdapter;
import android.support.v4.widget.SimpleCursorAdapter;
import android.view.View;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nostra13.universalimageloader.core.ImageLoader;
import no.tagstory.LoginFragmentActivity;
import no.tagstory.R;
import no.tagstory.utils.Database;

public class StatisticsActivity extends LoginFragmentActivity {

	private Database database;
	private Cursor statistics;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_statistics);

		database = new Database(this);
	}

	@Override
	protected void onResume() {
		super.onResume();

		database.open();
		statistics = database.getStatistics();
		ListView listView = (ListView) findViewById(R.id.list_statistics);
		listView.setAdapter(new SimpleCursorAdapter(this, R.layout.item_statistics,
				statistics,
				new String[]{Database.STATISTICS_DATE, Database.STATISTICS_DISTANCE}, new int[]{R.id.date, R.id.distance},
				CursorAdapter.FLAG_REGISTER_CONTENT_OBSERVER));

		TextView textView = (TextView) findViewById(R.id.read);
		if (statistics.getCount() > 0) {
			textView.setText(getString(R.string.statistic_read, statistics.getCount()));

			textView = (TextView) findViewById(R.id.walked);
			textView.setText(getString(R.string.statistic_walked, getDistance(statistics)));
		} else {
			textView.setVisibility(View.GONE);
			textView = (TextView) findViewById(R.id.walked);
			textView.setText(R.string.statistic_empty);
		}

	}

	private double getDistance(Cursor statistics) {
		if (statistics.getCount() == 0) {
			return 0;
		}

		int index = statistics.getColumnIndex(Database.STATISTICS_DISTANCE);
		statistics.moveToFirst();
		double distance = statistics.getDouble(index);
		while (statistics.moveToNext()) {
			distance += statistics.getDouble(index);
		}
		return distance;
	}

	@Override
	protected void onStop() {
		super.onStop();
		statistics.close();
		database.close();
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		super.onConnected(connectionHint);
		Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		ImageView imageView = (ImageView) findViewById(R.id.profile_image);
		imageView.setVisibility(View.VISIBLE);
		String url = currentPerson.getImage().getUrl().replaceFirst("sz=50$", "sz=200");
		ImageLoader.getInstance().displayImage(url, imageView);
	}
}
