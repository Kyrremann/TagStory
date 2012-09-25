package no.uio.ifi.inf5261.tagstory.story;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.List;

import no.uio.ifi.inf5261.tagstory.R;
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
import android.util.Log;
import android.widget.TextView;

public class StoryActivity extends Activity {

	private Story story;
	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);

		story = (Story) getIntent().getSerializableExtra("STORY");
		setTitle(story.getTitle());

		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	@Override
	protected void onNewIntent(Intent intent) {

		TextView textView = (TextView) findViewById(R.id.textView2);

		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		NdefRecord cardRecord = msg.getRecords()[0];
		String data = new String(cardRecord.getPayload());
		textView.setText(textView.getText() + "\n" + data);
	}

	public void enableForegroundMode() {
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
		IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
		nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent,
				writeTagFilters, null);
	}

	public void disableForegroundMode() {
		nfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	protected void onResume() {
		super.onResume();
		enableForegroundMode();
	}

	@Override
	protected void onPause() {
		super.onPause();
		disableForegroundMode();
	}
}
