package no.tagstory;

import no.tagstory.honeycomb.TagStoryActivityHoneyComb;

import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

public class LoadingActivity extends Activity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_loading);

		new Handler().postDelayed(new Runnable() {
			@Override
			public void run() {
				Intent intent = null;
				if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
					intent = new Intent(LoadingActivity.this, TagStoryActivityHoneyComb.class);
				} else {
					intent = new Intent(LoadingActivity.this, TagStoryActivity.class);
				}
				startActivity(intent);
				finish();
			}
		}, 1200);
	}

}
