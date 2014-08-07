package no.tagstory.story.activity.option.nfc;

import java.io.UnsupportedEncodingException;
import java.util.Arrays;

import no.tagstory.story.Story;
import no.tagstory.story.StoryTagOption;
import no.tagstory.story.activity.StoryActivity;
import android.annotation.TargetApi;
import android.app.PendingIntent;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.nfc.NdefMessage;
import android.nfc.NdefRecord;
import android.nfc.NfcAdapter;
import android.os.Build;
import android.os.Parcelable;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

@TargetApi(Build.VERSION_CODES.GINGERBREAD)
public abstract class NFCMapActivity extends FragmentActivity {

	protected Story story;
	protected StoryTagOption option;
	protected String partTag;

	protected NfcAdapter nfcAdapter;
	protected PendingIntent nfcPendingIntent;
	protected boolean nfcScanning;
	protected ProgressDialog progressDialog;
	
	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public void scanTag(View v) {
		startScanning();
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public void startScanning() {
		nfcScanning = true;
		nfcAdapter = NfcAdapter.getDefaultAdapter(this);

		nfcPendingIntent = PendingIntent.getActivity(this, 0, new Intent(this,
				this.getClass()).addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP), 0);

		enableForegroundMode();

		progressDialog = new ProgressDialog(this);
		progressDialog.setMessage("Searching for tag");
		progressDialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
			
			@Override
			public void onCancel(DialogInterface dialog) {
				cancelDialog(dialog);
			}
		});
		progressDialog.setButton(DialogInterface.BUTTON_NEUTRAL, "Abort",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						cancelDialog(dialog);
					}
				});
		progressDialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Cheat",
				new DialogInterface.OnClickListener() {

					@Override
					public void onClick(DialogInterface dialog, int which) {
						dialog.dismiss();
						progressDialog.dismiss();
						Intent intent = new Intent(getApplicationContext(),
								StoryActivity.class);
						intent.putExtra(StoryActivity.EXTRA_STORY, story);
						intent.putExtra(StoryActivity.EXTRA_TAG,
								option.getOptNext());
						startActivity(intent);
					}
				});
		progressDialog.setButton(DialogInterface.BUTTON_POSITIVE, "QR-Code", new DialogInterface.OnClickListener() {
			
			@Override
			public void onClick(DialogInterface dialog, int which) {
			Intent intent = new Intent("com.google.zxing.client.android.SCAN");
			intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_WHEN_TASK_RESET);

		    intent.putExtra("SCAN_MODE", "QR_CODE_MODE");
		    startActivityForResult(intent, 0);
			}
		});
		progressDialog.show();
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == 0) {
	        if (resultCode == RESULT_OK) {
	            //  The Intents Fairy has delivered us some data!
	            String contents = intent.getStringExtra("SCAN_RESULT");
	            String format = intent.getStringExtra("SCAN_RESULT_FORMAT");
	            // Handle successful scan
	            Log.d("QR", "Contents: " + contents);
	            Log.d("QR", "Format: " + format);
	            checkTagData(contents);
	        } else if (resultCode == RESULT_CANCELED) {
	            // Handle cancel
	        }
	    }
	}
	
	private void cancelDialog(DialogInterface dialog) {
		dialog.dismiss();
		progressDialog.dismiss();
		disableForegroundMode();
	}

	@Override
	public void onNewIntent(Intent intent) {
		progressDialog.setMessage("Reading tag");
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

		checkTagData(data.split("\n")[0]);
	}
	
	private void checkTagData(String tag) {
		// TODO: Implement randomized tags
		if (tag.equals(story.getTag(option.getOptNext())
				.getBelongsToTag())) {
			progressDialog.dismiss();
			Intent intent = new Intent(this, StoryActivity.class);
			intent.putExtra(StoryActivity.EXTRA_STORY, story);
			intent.putExtra(StoryActivity.EXTRA_TAG, option.getOptNext());
			startActivity(intent);
		} else {
			// TODO: What do to when user scan wrong tag?
			Toast.makeText(this, "Sorry, you're scanning the wrong tag",
					Toast.LENGTH_SHORT).show();
		}
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
	public void enableForegroundMode() {
		IntentFilter tagDetected = new IntentFilter(
				NfcAdapter.ACTION_TAG_DISCOVERED); // filter for all
		IntentFilter[] writeTagFilters = new IntentFilter[] { tagDetected };
		nfcAdapter.enableForegroundDispatch(this, nfcPendingIntent,
				writeTagFilters, null);
	}

	@TargetApi(Build.VERSION_CODES.GINGERBREAD_MR1)
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
