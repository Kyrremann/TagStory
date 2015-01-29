package no.tagstory.story.activity.utils;

import android.content.Context;
import android.content.Intent;
import no.tagstory.story.*;
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

	public static Intent createTravelIntent(Context context, Story story, StoryTag tag, StoryTagOption option) {
		return createTravelIntent(context, story, tag, option, false);
	}

	// TODO: Need to change from travel indent to show answer intent, and after then I can use the travel intent
	// I think this comment has something to do with the quiz returning before it should
	public static Intent createTravelIntent(Context context, Story story,
	                                        StoryTag tag, StoryTagOption option,
	                                        boolean fromPropagating) {

		Intent intent = new Intent();
		intent.putExtra(EXTRA_STORY, story);
		intent.putExtra(OPTION, option);
		intent.putExtra(EXTRA_TAG, tag.getUUID());

		if (!fromPropagating && option.getPropagatingText() != null) {
			intent.setClass(context, StoryPropagatingActivity.class);
			return intent;
		}

		TagTypeEnum tagType = tag.getTagType();
		HintMethodEnum method = option.getMethod();

		if (tagType.isGPS()) {
			intent.setClass(context, getGPSHintClass(method));
		} else if (tagType.isQR()) {
			intent.setClass(context, getQRHintClass(method));
		} else if (tagType.isNFC()) {
			// TODO Implement NFC
		} else if (tagType.isBLE()) {
			// TODO Implement BLE
		} else {
			throw new UnsupportedOperationException("Unsupported tag type: " + tagType);
		}

		return intent;
	}

	private static Class<?> getGPSHintClass(HintMethodEnum method) {
		if (method.isSound()) {
			return AudioPlayerActivity.class;
		} else if (method.isMap()) {
			return GPSMapNavigationActivity.class;
		} else {
			return GPSActivity.class;
		}
	}

	private static Class<?> getQRHintClass(HintMethodEnum method) {
		if (method.isSound()) {
			return AudioPlayerActivity.class;
		} else if (method.isMap()) {
			return MapNavigationActivity.class;
		} else {
			return StoryTravelActivity.class;
		}
	}
}
