package no.tagstory.story.activity.option.gps;

import java.io.IOException;

import no.tagstory.kines_bursdag.R;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;

public class GPSAudioPlayerActivity extends GPSActivity implements	OnPreparedListener, MediaController.MediaPlayerControl {

	private static final String TAG = "AudioPlayer";
	private static String AUDIO_FILE_NAME;

	private MediaPlayer mediaPlayer;
	private MediaController mediaController;

	private Handler handler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story_audioplayer);
		// getActionBar().setDisplayHomeAsUpEnabled(true);
/*
		Bundle bundle = getIntent().getExtras();
		option = (StoryPartOption) bundle
				.getSerializable(StoryTravelActivity.OPTION);
		story = (Story) bundle.getSerializable(StoryActivity.EXTRA_STORY);
		tagId = bundle.getString(StoryActivity.EXTRA_TAG);
		previousTag = bundle.getString(StoryActivity.PREVIOUSTAG);*/

		AUDIO_FILE_NAME = option.getOptSoundSrc();
		((TextView) findViewById(R.id.story_audio_song)).setText(AUDIO_FILE_NAME);
		hintText.setText(option.getOptHintText());

		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(this);

		mediaController = new MediaController(this);

		try {
			mediaPlayer.setDataSource(getResources().getAssets()
					.openFd(AUDIO_FILE_NAME).getFileDescriptor());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			Toast.makeText(this, "Something went wrong,  we are very sorry",
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Could not open file " + AUDIO_FILE_NAME + " for playback.", e);
		}
	}

	@Override
	protected void onPause() {
		super.onPause();
		pause();
	}

	@Override
	public boolean onTouchEvent(MotionEvent event) {
		// the MediaController will hide after 3 seconds - tap the screen to
		// make it appear again
		mediaController.show();
		return false;
	}

	@Override
	public void start() {
		mediaPlayer.start();
	}

	@Override
	public void pause() {
		mediaPlayer.pause();
	}

	@Override
	public int getDuration() {
		return mediaPlayer.getDuration();
	}

	@Override
	public int getCurrentPosition() {
		return mediaPlayer.getCurrentPosition();
	}

	@Override
	public void seekTo(int i) {
		mediaPlayer.seekTo(i);
	}

	@Override
	public boolean isPlaying() {
		return mediaPlayer.isPlaying();
	}

	@Override
	public int getBufferPercentage() {
		return 0;
	}

	@Override
	public boolean canPause() {
		return true;
	}

	@Override
	public boolean canSeekBackward() {
		return true;
	}

	@Override
	public boolean canSeekForward() {
		return true;
	}

	@Override
	public int getAudioSessionId() {
		return 0;
	}

	@Override
	public void onPrepared(MediaPlayer mediaPlayer) {
		Log.d(TAG, "onPrepared");
		mediaController.setMediaPlayer(this);
		mediaController.setAnchorView(findViewById(R.id.story_audio_layout));

		handler.post(new Runnable() {
			@Override
			public void run() {
				mediaController.setEnabled(true);
				mediaController.show();
			}
		});
	}
}
