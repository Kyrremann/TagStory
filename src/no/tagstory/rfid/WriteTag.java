package no.tagstory.rfid;

import java.io.IOException;

import android.app.Activity;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.FormatException;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.nfc.Tag;
import android.nfc.tech.Ndef;

public class WriteTag {

	private String message;
	private Context context;
	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;

	public WriteTag(Context context, String message) {
		this.context = context;
		this.message = message;

		nfcAdapter = NfcAdapter.getDefaultAdapter(context);
		nfcPendingIntent = PendingIntent.getActivity(context, 0, new Intent(
				context, context.getClass())
				.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);
	}

	public void onNewIntent(Intent intent) {
		Tag tag = intent.getParcelableExtra(NfcAdapter.EXTRA_TAG);

		Ndef ndef = Ndef.get(tag);
		if (ndef != null) {

			try {
				String lang = "en";
				byte[] textBytes = message.getBytes();
				byte[] langBytes = lang.getBytes("UTF_8");
				int langLength = langBytes.length;
				int textLength = textBytes.length;
				byte[] payload = new byte[1 + langLength + textLength];

				// set status byte (see NDEF spec for actual bits)
				payload[0] = (byte) langLength;

				// copy langbytes and textbytes into payload
				System.arraycopy(langBytes, 0, payload, 1, langLength);
				System.arraycopy(textBytes, 0, payload, 1 + langLength,
						textLength);

				NdefRecord record = new NdefRecord(NdefRecord.TNF_WELL_KNOWN,
						NdefRecord.RTD_TEXT, new byte[0], payload);

				NdefMessage tagMessage = null; //new NdefMessage(record);

				ndef.connect();
				ndef.writeNdefMessage(tagMessage);
				ndef.close();
			} catch (IOException e) {
				e.printStackTrace();
			} catch (FormatException e) {
				e.printStackTrace();
			}
		} else {
			// TODO: Non-formatted tag
		}

	}

	public void enableForegroundMode() {
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
		IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
		nfcAdapter.enableForegroundDispatch((Activity) context,
				nfcPendingIntent, writeTagFilters, null);
	}

	public void disableForegroundMode() {
		nfcAdapter.disableForegroundDispatch((Activity) context);
	}

}
