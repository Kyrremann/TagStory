package no.tagstory.rfid;

import android.annotation.TargetApi;
import android.app.Activity;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Bundle;
import android.os.Parcelable;
import android.widget.TextView;
import no.tagstory.R;

@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
public class DetectStory extends Activity {

	public DetectStory() {
	}

	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_story);

		// initialize NFC
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);
		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
		onNewIntent(getIntent());

		// IntentFilter ndef = new
		// IntentFilter(NfcAdapter.ACTION_NDEF_DISCOVERED);
		// try {
		// ndef.addDataType("*/*"); /* Handles all MIME based dispatches.
		// You should specify only the ones that you need. */
		// }
		// catch (MalformedMimeTypeException e) {
		// throw new RuntimeException("fail", e);
		// }
		// IntentFilter[] intentFiltersArray = new IntentFilter[] {ndef, };
	}

	public void enableForegroundMode() {
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
		IntentFilter[] writeTagFilters = new IntentFilter[]{tagDetected};
		nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent,
				writeTagFilters, null);
	}

	public void disableForegroundMode() {
		nfcAdapter.disableForegroundDispatch(this);
	}

	@Override
	public void onNewIntent(Intent intent) {
		TextView textView = (TextView) findViewById(R.id.description);
		String action = intent.getAction();

		if (NfcAdapter.ACTION_NDEF_DISCOVERED.equals(action)
				|| NfcAdapter.ACTION_TAG_DISCOVERED.equals(action)) {
			Parcelable[] messages = intent
					.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);

			if (messages != null) {
				Parcelable[] rawMsgs = intent
						.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
				NdefMessage msg = (NdefMessage) rawMsgs[0];
				NdefRecord cardRecord = msg.getRecords()[0];
				String data = new String(cardRecord.getPayload());
				textView.setText(textView.getText() + "\n" + data);
			}
		} else {
			textView.setText("Tag " + intent.getAction()
					+ " is not used by our system. ");
			// ignore
		}
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