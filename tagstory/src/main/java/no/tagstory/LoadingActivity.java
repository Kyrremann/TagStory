package no.tagstory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.s3.transfermanager.Download;
import com.amazonaws.mobileconnectors.s3.transfermanager.TransferManager;
import com.amazonaws.regions.Region;
import com.amazonaws.regions.Regions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import no.tagstory.honeycomb.TagStoryActivityHoneycomb;

import java.io.File;

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

		final CognitoCachingCredentialsProvider cognitoProvider = new CognitoCachingCredentialsProvider(
				this, // get the context for the current activity
				"", /* Identity Pool ID */
				Regions.EU_WEST_1 /* Region */
		);
		new Thread(new Runnable() {
			@Override
			public void run() {
				TransferManager transferManager = new TransferManager(cognitoProvider);
				transferManager.getAmazonS3Client().setRegion(Region.getRegion(Regions.EU_WEST_1));
				Download download = transferManager.download("tagstory", "images/dfcfe0105271fa8cd21ce32ad8349f6b8909a28e79b2a0f27e47f5ca91f6becc.jpg", getFileStreamPath("dfcfe0105271fa8cd21ce32ad8349f6b8909a28e79b2a0f27e47f5ca91f6becc.jpg"));
				System.out.println(download);
				while (!download.isDone()) ;//System.out.println("Downloading..." + download.getProgress().getPercentTransferred());
				System.out.println(download);
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
