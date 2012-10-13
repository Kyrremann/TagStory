package no.uio.ifi.inf5261.tagstory.story;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.List;

import no.uio.ifi.inf5261.tagstory.R;
import no.uio.ifi.inf5261.tagstory.StoryDetailActivity;
import no.uio.ifi.inf5261.tagstory.StoryListActivity;
import no.uio.ifi.inf5261.tagstory.story.option.ArrowNavigationActivity;
import no.uio.ifi.inf5261.tagstory.story.option.AudioPlayerActivity;
import no.uio.ifi.inf5261.tagstory.story.option.MapNavigationActivity;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.MifareUltralight;
import android.nfc.tech.Ndef;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v4.app.NavUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

public class StoryActivity extends Activity {

	public static final String STORY = "STORY";
	public static final String PARTTAG = "TAG";
	public static final String PREVIOUSTAG = "PREVIOUS";
	private Story story;
	private StoryPart part;
	private String partTag, previousTag;

	// protected NfcAdapter nfcAdapter;
	// protected PendingIntent nfcPendingIntent;

	// private TextView storyStart;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);
		getActionBar().setDisplayHomeAsUpEnabled(true);

		story = (Story) getIntent().getSerializableExtra(STORY);
		setTitle(story.getTitle());
		partTag = getIntent().getStringExtra(PARTTAG);
		previousTag = getIntent().getStringExtra(PREVIOUSTAG);
		part = story.getStoryPart(partTag);

		((TextView) findViewById(R.id.story_part_uuid)).setText("UUID: "
				+ part.getUUID());
		((TextView) findViewById(R.id.story_part_belongsto))
				.setText("Belongs to: " + part.getBelongsToTag());
		((TextView) findViewById(R.id.story_part_desc)).setText("Description\n"
				+ part.getDescription());
		((TextView) findViewById(R.id.story_part_choice)).setText("Choice\n"
				+ part.getChoiceDescription());

		if (!part.getIsEndpoint()) {
			View view = getProperOptionView(part.getOptions());
			((LinearLayout) findViewById(R.id.story_part_option)).addView(view);
		} else {
			Toast.makeText(
					this,
					"You've done it! Thank you for using Tag Story. hope you enjoyed the ride.",
					Toast.LENGTH_SHORT).show();
			finish();
		}

		// LinearLayout layout = (LinearLayout)
		// findViewById(R.id.story_part_option);

		// storyStart = (TextView) findViewById(R.id.textView1);

		// nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		// nfcPendingIntent = PendingIntent.getActivity(this, 0, new
		// Intent(this,
		// this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	private View getProperOptionView(
			final HashMap<String, StoryPartOption> options) {

		View view;
		if (options.size() == 1) {
			final StoryPartOption option = options.values().iterator().next();

			Button button = new Button(this);
			button.setText(option.getUUID());
			button.setOnClickListener(new OnClickListener() {

				@Override
				public void onClick(View view) {
					Intent intent = createTravelIntent(option);
					startActivity(intent);
				}
			});
			// TODO: Make a listener for this one button
			// Also send the option with the intent
			view = button;
		} else {
			ListView listView = new ListView(this);
			final String[] keys = options.keySet().toArray(
					new String[options.size()]);
			listView.setAdapter(new ArrayAdapter<String>(this,
					android.R.layout.simple_list_item_1, keys));
			listView.setOnItemClickListener(new OnItemClickListener() {

				@Override
				public void onItemClick(AdapterView<?> parent, View view,
						int position, long id) {
					Intent intent = createTravelIntent(options
							.get(keys[position]));
					startActivity(intent);
				}
			});
			view = listView;
		}

		return view;
	}

	private Intent createTravelIntent(StoryPartOption option) {
		Intent intent = new Intent();
		intent.putExtra(STORY, story);
		intent.putExtra(StoryTravelActivity.OPTION, option);
		intent.putExtra(PARTTAG, partTag);
		intent.putExtra(PREVIOUSTAG, previousTag);
		String opt = option.getOptSelectMethod();
		if (opt.equals(StoryPartOption.HINT_SOUND))
			intent.setClass(this, AudioPlayerActivity.class);
		else if (opt.equals(StoryPartOption.HINT_ARROW))
			intent.setClass(this, ArrowNavigationActivity.class);
		else if (opt.equals(StoryPartOption.HINT_MAP))
			intent.setClass(this, MapNavigationActivity.class);
		else
			intent.setClass(this, StoryTravelActivity.class);

		return intent;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		if (item.getItemId() == android.R.id.home) {
			Intent intent;
			if (previousTag == null) {
				intent = new Intent(this, StoryDetailActivity.class);
				intent.putExtra(STORY, story);
			} else {
				intent = new Intent(this, StoryActivity.class);
				intent.putExtra(STORY, story);
				intent.putExtra(StoryActivity.PARTTAG, partTag);
			}
			NavUtils.navigateUpTo(this, intent);
			return true;
		}

		return super.onOptionsItemSelected(item);
	}

	// @Override
	// protected void onNewIntent(Intent intent) {
	//
	// TextView textView = (TextView) findViewById(R.id.textView2);
	//
	// Parcelable[] rawMsgs = intent
	// .getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
	// NdefMessage msg = (NdefMessage) rawMsgs[0];
	// NdefRecord cardRecord = msg.getRecords()[0];
	// String data = new String(cardRecord.getPayload());
	// textView.setText(textView.getText() + "\n" + data);
	// }
	//
	// public void enableForegroundMode() {
	// IntentFilter tagDetected = new IntentFilter(
	// NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
	// IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
	// nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent,
	// writeTagFilters, null);
	// }
	//
	// public void disableForegroundMode() {
	// nfcAdapter.disableForegroundDispatch(this);
	// }
	//
	// @Override
	// protected void onResume() {
	// super.onResume();
	// enableForegroundMode();
	// }
	//
	// @Override
	// protected void onPause() {
	// super.onPause();
	// disableForegroundMode();
	// }
}
