package no.tagstory.story.activity.utils;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.StoryTagOption;
import no.tagstory.story.activity.StoryPropagatingActivity;
import no.tagstory.story.activity.StoryTravelActivity;
import no.tagstory.story.activity.option.AudioPlayerActivity;
import no.tagstory.story.activity.option.MapNavigationActivity;
import no.tagstory.story.activity.option.gps.GPSActivity;
import no.tagstory.story.activity.option.gps.GPSMapNavigationActivity;
import no.tagstory.story.game.CameraActivity;
import no.tagstory.story.game.QuizActivity;

import static no.tagstory.story.activity.StoryActivity.EXTRA_STORY;
import static no.tagstory.story.activity.StoryActivity.EXTRA_TAG;
import static no.tagstory.story.activity.StoryTravelActivity.OPTION;

public class TravelIntentUtil {


	public static Intent createCameraActivity(Context context, Story story, String tagId) {
		Intent intent;
		intent = new Intent(context, CameraActivity.class);
		intent.putExtra(EXTRA_STORY, story);
		intent.putExtra(EXTRA_TAG, tagId);
		return intent;
	}

	public static Intent createQuizActivity(Context context, Story story, String tagId) {
		Intent intent;
		intent = new Intent(context, QuizActivity.class);
		intent.putExtra(EXTRA_STORY, story);
		intent.putExtra(EXTRA_TAG, tagId);
		return intent;
	}

	public static Intent createTravelIntent(Context context, Story story,
	                                        StoryTag tag, StoryTagOption option) {
		return createTravelIntent(context, story, tag, option,
				false);
	}

	// TODO: Need to change from travel indent to show answer intent, and after
	// then I can use the travel intent
	public static Intent createTravelIntent(Context context, Story story,
	                                        StoryTag tag, StoryTagOption option,
	                                        boolean fromPropagating) {

		Intent intent = new Intent();
		intent.putExtra(EXTRA_STORY, story);
		intent.putExtra(OPTION, option);
		intent.putExtra(EXTRA_TAG, tag.getUUID());

		if (!fromPropagating && option.getOptPropagatingText() != null) {
			intent.setClass(context, StoryPropagatingActivity.class);
			return intent;
		}

		String tagMode = tag.getTagMode();
		String opt = option.getOptSelectMethod();

		if (tagMode.equals(StoryTag.TAG_GPS)) {
			intent.setClass(context, getGPSHintClass(opt));
		} else if (tagMode.equals(StoryTag.TAG_QR)) {
			intent.setClass(context, getQRHintClass(opt));
		} else if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_NFC)) {
			// TODO Device has NFC
		} else {
			throw new UnsupportedOperationException("Unsupported tagmode: " + tagMode);
		}

		return intent;
	}

	private static Class<?> getGPSHintClass(String optionHint) {
		if (optionHint.equals(StoryTagOption.HINT_SOUND)) {
			return AudioPlayerActivity.class;
		} else if (optionHint.equals(StoryTagOption.HINT_MAP)) {
			return GPSMapNavigationActivity.class;
		} else {
			return GPSActivity.class;
		}
	}

	private static Class<?> getQRHintClass(String optionHint) {
		if (optionHint.equals(StoryTagOption.HINT_SOUND)) {
			return AudioPlayerActivity.class;
		} else if (optionHint.equals(StoryTagOption.HINT_MAP)) {
			return MapNavigationActivity.class;
		} else {
			return StoryTravelActivity.class;
		}
	}
}
