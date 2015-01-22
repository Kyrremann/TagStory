package no.tagstory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import no.tagstory.honeycomb.TagStoryActivityHoneycomb;

import static no.tagstory.utils.ClassVersionFactory.createIntent;

public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);
		setTitle(R.string.app_title);

		new Thread(new Runnable() {
			@Override
			public void run() {
				ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext())
						.build();
				ImageLoader.getInstance().init(config);
			}
		}).start();

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = createIntent(getApplicationContext(),
						TagStoryActivityHoneycomb.class, TagStoryActivity.class);
				startActivity(intent);
				finish();
			}
		}, 1200);
	}

}
