package no.uio.ifi.inf5261.tagstory.story;

import no.uio.ifi.inf5261.tagstory.R;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Bundle;
import android.os.Parcelable;
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
		
		Parcelable[] rawMsgs = intent.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];
	    NdefRecord cardRecord = msg.getRecords()[0];
	    String data = new String(cardRecord.getPayload());
	    textView.setText(textView.getText() + "\n" + data);
//
//		if (NfcAdapter.ACTION_TAG_DISCOVERED.equals(intent.getAction())
//				|| NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//			TextView textView = (TextView) findViewById(R.id.textView2);
//
//			Parcelable[] messages = intent
//					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
//			if (messages != null) {
//
//				NdefMessageDecoder ndefMessageDecoder = NdefContext
//						.getNdefMessageDecoder();
//				// parse to records - byte to POJO
//				for (int i = 0; i < messages.length; i++) {
//					List<Record> records = ndefMessageDecoder
//							.decodeToRecords(((NdefMessage) messages[i])
//									.toByteArray());
//
//					for (int k = 0; k < records.size(); k++) {
//						textView.setText(textView.getText() + "\n"
//								+ records.get(k).toString());
//					}
//				}
//			}
//		} else if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(intent.getAction())) {
//			System.out.println("NDEF");
//		} else {
//			System.out.println("IGNORE");
//			// ignore
//		}
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
