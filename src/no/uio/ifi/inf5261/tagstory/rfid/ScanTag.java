package no.uio.ifi.inf5261.tagstory.rfid;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;

public class ScanTag {

	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;
	private Context context;

	public ScanTag(Context context) {
		this.context = context;
		nfcAdapter = NfcAdapter.getDefaultAdapter(context);
		
		nfcPendingIntent = PendingIntent.getActivity(context, 0, new Intent(
				context, this.getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
 
		enableForegroundMode();
		// onNewIntent(((Activity) context).getIntent());
	}

	public void onNewIntent(Intent intent) {
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		NdefRecord cardRecord = msg.getRecords()[0];
		String data = new String(cardRecord.getPayload());
		System.out.println(data);
	}

	public void enableForegroundMode() {
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
		IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
		nfcAdapter.enableForegroundDispatch((Activity) context,
				nfcPendingIntent, null, null);
	}

	public void disableForegroundMode() {
		nfcAdapter.disableForegroundDispatch((Activity) context);
	}

}
