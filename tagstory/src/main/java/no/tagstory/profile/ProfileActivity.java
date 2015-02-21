package no.tagstory.profile;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ImageView;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;
import com.nostra13.universalimageloader.core.ImageLoader;
import no.tagstory.LoginFragmentActivity;
import no.tagstory.R;

public class ProfileActivity extends LoginFragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_profile);
	}

	@Override
	public void onConnected(Bundle connectionHint) {
		super.onConnected(connectionHint);
		Person currentPerson = Plus.PeopleApi.getCurrentPerson(mGoogleApiClient);
		ImageView imageView = (ImageView) findViewById(R.id.profile_image);
		String url = currentPerson.getImage().getUrl().replaceFirst("sz=50$", "sz=200");
		ImageLoader.getInstance().displayImage(url, imageView);
	}
}
