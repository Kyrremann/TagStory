package no.tagstory.story.activity.option;

import android.media.AudioManager;
import android.media.MediaPlayer;
import android.media.MediaPlayer.OnPreparedListener;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.Toast;
import no.tagstory.R;
import no.tagstory.story.activity.StoryTravelActivity;

import java.io.IOException;

public class AudioPlayerActivity extends StoryTravelActivity implements
		OnPreparedListener, MediaController.MediaPlayerControl {

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

		try {
			Uri myUri = Uri.parse(getFileStreamPath(AUDIO_FILE_NAME).getPath());
			mediaPlayer = new MediaPlayer();
			mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC);
			mediaPlayer.setDataSource(getApplicationContext(), myUri);
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			e.printStackTrace();
			Toast.makeText(this, "Something went wrong,  we are very sorry",
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Could not open file " + AUDIO_FILE_NAME + " for playback.", e);
		}
/*
		mediaPlayer = new MediaPlayer();
		mediaPlayer.setOnPreparedListener(this);

		mediaController = new MediaController(this);

		try {
			mediaPlayer.setDataSource(getFileStreamPath(AUDIO_FILE_NAME).getPath());
			mediaPlayer.prepare();
			mediaPlayer.start();
		} catch (IOException e) {
			Toast.makeText(this, "Something went wrong,  we are very sorry",
					Toast.LENGTH_SHORT).show();
			Log.e(TAG, "Could not open file " + AUDIO_FILE_NAME + " for playback.", e);
		}
		*/
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
