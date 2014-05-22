package no.tagstory.story.game;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.WebView;
import no.tagstory.kines_bursdag.R;
import no.tagstory.story.Story;
import no.tagstory.story.StoryPart;
import no.tagstory.story.StoryPartOption;
import no.tagstory.story.activity.StoryActivity;

import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;
import java.util.UUID;

public class CameraActivity extends Activity {

	private static final int CAMERA_REQUEST = 1888;
	static final int REQUEST_TAKE_PHOTO = 1;
	private String selectedImagePath;
	WebView webview;
	String fileName = "capturedImage.jpg";
	private static Uri mCapturedImageURI;
	private static Uri uri;
	String newimagename, mCurrentPhotoPath;

	private Story story;
	private StoryPart part;
	private String partTag; //, previousTag;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		// TODO Add a view where user is asked to take a picture
		setContentView(R.layout.activity_story_camera);

		Bundle bundle = getIntent().getExtras();
		story = (Story) bundle.getSerializable(StoryActivity.STORY);
		partTag = bundle.getString(StoryActivity.PARTTAG);
		// previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);
		part = story.getStoryPart(partTag);
		newimagename = UUID.randomUUID() + ".jpg";
		dispatchTakePictureIntent();
	}

	private File createImageFile() throws IOException {
		// Create an image file name
		String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
		String imageFileName = "JPEG_" + timeStamp + "_";
		File storageDir = Environment.getExternalStoragePublicDirectory(
				Environment.DIRECTORY_PICTURES);
		//File image = File.createTempFile(
		//		imageFileName,  /* prefix */
		//		".jpg",         /* suffix */
		//		storageDir      /* directory */
		//);
		File image =  new File(
				Environment.getExternalStoragePublicDirectory(
						Environment.DIRECTORY_DOWNLOADS),
				imageFileName + ".jpg");

		// Save a file: path for use with ACTION_VIEW intents
		mCurrentPhotoPath = "file:" + image.getAbsolutePath();
		return image;
	}

	private void dispatchTakePictureIntent() {
		Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
		// Ensure that there's a camera activity to handle the intent
		if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
			// Create the File where the photo should go
			File photoFile = null;
			try {
				photoFile = createImageFile();
			} catch (IOException ex) {
				// Error occurred while creating the File
				ex.printStackTrace();
			}
			// Continue only if the File was successfully created
			if (photoFile != null) {
				uri = Uri.fromFile(photoFile);
				takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT,
						uri);
				startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
			}
		}
	}

	public void done(View v) {
		if (v.getId() == R.id.camera_button) {
			final StoryPartOption option = part.getOptions().values()
					.iterator().next();
			Intent intent = StoryActivity.createTravelIntent(
					getApplicationContext(), story, story.getStoryPart(partTag), option,
					option.getOptNext(), partTag);
			startActivity(intent);
		} else if (v.getId() == R.id.camera_button_retry) {
			dispatchTakePictureIntent();
		}
	}

	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode == RESULT_OK) {
			if (requestCode == REQUEST_TAKE_PHOTO) {
				// uri = f.getAbsolutePath();
				Intent picMessageIntent = new Intent(android.content.Intent.ACTION_SEND);
				picMessageIntent.setType("image/jpg");
				picMessageIntent.putExtra(Intent.EXTRA_STREAM, uri);
				startActivityForResult(Intent.createChooser(picMessageIntent, "Send image using"), RESULT_OK);
				return;
			}
		}
	}


}
