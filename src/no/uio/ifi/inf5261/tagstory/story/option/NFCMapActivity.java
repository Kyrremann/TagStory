package no.uio.ifi.inf5261.tagstory.story.option;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import com.google.android.maps.MapActivity;

import no.uio.ifi.inf5261.tagstory.story.Story;
import no.uio.ifi.inf5261.tagstory.story.StoryActivity;
import no.uio.ifi.inf5261.tagstory.story.StoryPartOption;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Parcelable;
import android.widget.Button;
import android.widget.Toast;

public abstract class NFCMapActivity extends MapActivity {

	protected Story story;
	protected StoryPartOption option;
	protected String partTag;

	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;
	protected boolean nfcScanning;
	protected ProgressDialog progressDialog;

	public void startScanning() {
		nfcScanning = true;
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		enableForegroundMode();

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Searching for tag");
		progressDialog.setButton(ProgressDialog.BUTTON_NEUTRAL, "Abort",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						progressDialog.dismiss();
					}
				});
		progressDialog.setButton(ProgressDialog.BUTTON_NEGATIVE, "Cheat",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						progressDialog.dismiss();
						Intent intent = new Intent(getApplicationContext(),
								StoryActivity.class);
						intent.putExtra(StoryActivity.STORY, story);
						intent.putExtra(StoryActivity.PARTTAG,
								option.getOptNext());
						intent.putExtra(StoryActivity.PREVIOUSTAG, partTag);
						startActivity(intent);
					}
				});
		progressDialog.setCancelable(false);
		progressDialog.show();
	}

	@Override
	public void onNewIntent(Intent intent) {
		progressDialog.setMessage("Scanning tag");
		Parcelable[] rawMsgs = intent
				.getParcelableArrayExtra(NfcAdapter.EXTRA_NDEF_MESSAGES);
		NdefMessage msg = (NdefMessage) rawMsgs[0];
		NdefRecord cardRecord = msg.getRecords()[0];
		String data = "";
		if (cardRecord.getTnf() == NdefRecord.TNF_WELL_KNOWN
				&& Arrays.equals(cardRecord.getType(), NdefRecord.RTD_TEXT)) {
			try {
				byte[] payload = cardRecord.getPayload();

				/*
				 * payload[0] contains the "Status Byte Encodings" field, per
				 * the NFC Forum "Text Record Type Definition" section 3.2.1.
				 * 
				 * bit7 is the Text Encoding Field. if (Bit_7 == 0): The text is
				 * encoded in UTF-8 if (Bit_7 == 1): The text is encoded in
				 * UTF16 Bit_6 is reserved for future use and must be set to
				 * zero. Bits 5 to 0 are the length of the IANA language code.
				 */

				// Get the Text Encoding
				String textEncoding = ((payload[0] & 0200) == 0) ? "UTF-8"
						: "UTF-16";

				// Get the Language Code
				int languageCodeLength = payload[0] & 0077;
				@SuppressWarnings("unused")
				String languageCode = new String(payload, 1,
						languageCodeLength, "US-ASCII");

				// Get the Text
				data = new String(payload, languageCodeLength + 1,
						payload.length - languageCodeLength - 1, textEncoding);
			} catch (UnsupportedEncodingException e) {

			}
		}

		if (data.split("\n")[0].equals(story.getStoryPart(option.getOptNext())
				.getBelongsToTag())) {
			progressDialog.dismiss();
			intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.STORY, story);
			intent.putExtra(StoryActivity.PARTTAG, option.getOptNext());
			intent.putExtra(StoryActivity.PREVIOUSTAG, partTag);
			startActivity(intent);
		} else {
			// TODO: What do to when user scan wrong tag?
			Toast.makeText(this, "Sorry, you're scanning the wrong tag",
					Toast.LENGTH_SHORT).show();
		}
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
		if (nfcScanning)
			enableForegroundMode();
	}

	@Override
	protected void onPause() {
		super.onPause();
		if (nfcScanning)
			disableForegroundMode();
	}

}
