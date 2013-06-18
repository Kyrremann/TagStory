package no.tagstory.story.activity;

import java.util.HashMap;

import no.tagstory.R;
import no.tagstory.Database.JsonParser;
import no.tagstory.story.Story;
import no.tagstory.story.StoryPart;
import no.tagstory.story.StoryPartOption;
import no.tagstory.story.game.QuizActivity;
import no.tagstory.story.activity.option.ArrowNavigationActivity;
import no.tagstory.story.activity.option.AudioPlayerActivity;
import no.tagstory.story.activity.option.MapNavigationActivity;
import no.tagstory.story.activity.option.gps.GPSActivity;
import no.tagstory.story.activity.option.gps.GPSMapNavigationActivity;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;

public class StoryActivity extends Activity {

	public static final String STORY = "STORY";
	public static final String PARTTAG = "TAG";
	public static final String PREVIOUSTAG = "PREVIOUS";

	protected Story story;
	private StoryPart part;
	protected String partTag;
	protected String previousTag;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);

		story = (Story) getIntent().getSerializableExtra(STORY);
		setTitle(story.getTitle()); // TODO: Should be part title
		partTag = getIntent().getStringExtra(PARTTAG);
		part = story.getStoryPart(partTag);
		// TODO: previous tag not working properly, should be a list
		previousTag = getIntent().getStringExtra(PREVIOUSTAG);

		((TextView) findViewById(R.id.story_part_desc)).setText(part
				.getDescription());
		if (part.getChoiceDescription() != null
				&& part.getChoiceDescription().length() != 0) {
			((TextView) findViewById(R.id.story_part_choice)).setText(part
					.getChoiceDescription());
		} else {
			((TextView) findViewById(R.id.story_part_choice))
					.setVisibility(View.GONE);
			// .setBackgroundResource(0);
		}

		if (!part.isEndpoint()) {
			generateOptionFunction(part.getOptions());
		} else {
			// TODO: Should be handled by the travel activity
			Intent intent = new Intent(this, StoryFinishedActivity.class);
			intent.putExtra(STORY, story);
			intent.putExtra(PARTTAG, partTag);
			intent.putExtra(PREVIOUSTAG, previousTag);
			startActivity(intent);
		}
	}

	private void generateOptionFunction(
			final HashMap<String, StoryPartOption> options) {
		Button button = (Button) findViewById(R.id.story_activity_travel);

		if (options.size() == 1) {
			final StoryPartOption option = options.values().iterator().next();

			if (part.getGameMode() != null
					&& part.getGameMode().equals(JsonParser.QUIZ))
				button.setText(part.getGameButton());
			else
				button.setText(option.getUUID());

			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent intent;
					if (part.getGameMode().equals(JsonParser.QUIZ)) {
						intent = new Intent(getApplicationContext(),
								QuizActivity.class);
						intent.putExtra(StoryActivity.STORY, story);
						intent.putExtra(StoryActivity.PARTTAG, partTag);
						intent.putExtra(StoryActivity.PREVIOUSTAG, previousTag);
					} else {
						intent = createTravelIntent(getApplicationContext(),
								story, part, option, partTag, previousTag);
					}
					startActivity(intent);
				}
			});
		} else {
			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			// builder.setTitle("Choose you future");
			// TODO: Set max length on choice description, or find an new way to
			// show it
			// builder.setTitle(part.getChoiceDescription());

			final String[] keys = options.keySet().toArray(
					new String[options.size()]);

			builder.setSingleChoiceItems(keys, -1,
					new DialogInterface.OnClickListener() {

						@Override
						public void onClick(DialogInterface dialog, int which) {
							// System.out.println(((AlertDialog)
							// dialog).getListView().getItemAtPosition(which));
							startActivity(createTravelIntent(
									getApplicationContext(), story, part,
									options.get(((AlertDialog) dialog)
											.getListView().getItemAtPosition(
													which)), partTag,
									previousTag));
						}
					});

			final Dialog dialog = builder.create();

			button.setText(R.string.story_find_next);
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					dialog.show();
				}
			});
		}
	}

	// TODO: This is where I need to check for what kind of scanning of tag is
	// used, either gps, qr or nfc. Or all of them :\
	public static Intent createTravelIntent(Context context, Story story,
			StoryPart part, StoryPartOption option, String partTag,
			String previousTag) {
		Intent intent = new Intent();
		intent.putExtra(STORY, story);
		intent.putExtra(StoryTravelActivity.OPTION, option);
		intent.putExtra(PARTTAG, partTag);
		intent.putExtra(PREVIOUSTAG, previousTag);
		String tagMode = part.getTagMode();
		String opt = option.getOptSelectMethod();

		if (tagMode.equals(StoryPart.TAG_GPS)) {
			intent.setClass(context, getGPSHintClass(opt));
		} else if (tagMode.equals(StoryPart.TAG_QR)) {
			intent.setClass(context, getQRHintClass(opt));
		} else if (context.getPackageManager().hasSystemFeature(
				PackageManager.FEATURE_NFC)) { // Device has NFC
		}

		return intent;
	}

	private static Class<?> getGPSHintClass(String optionHint) {
		if (optionHint.equals(StoryPartOption.HINT_SOUND))
			return AudioPlayerActivity.class;
		// else if (optionHint.equals(StoryPartOption.HINT_ARROW))
		// return ArrowNavigationActivity.class;
		else if (optionHint.equals(StoryPartOption.HINT_MAP))
			return GPSMapNavigationActivity.class;
		else
			return GPSActivity.class;
	}

	private static Class<?> getQRHintClass(String optionHint) {
		if (optionHint.equals(StoryPartOption.HINT_SOUND))
			return AudioPlayerActivity.class;
		// else if (optionHint.equals(StoryPartOption.HINT_ARROW))
		// return ArrowNavigationActivity.class;
		else if (optionHint.equals(StoryPartOption.HINT_MAP))
			return MapNavigationActivity.class;
		else
			return StoryTravelActivity.class;
	}
}
