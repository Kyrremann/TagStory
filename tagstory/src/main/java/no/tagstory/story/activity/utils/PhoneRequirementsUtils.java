package no.tagstory.story.activity.utils;

import android.content.Context;
import android.content.pm.PackageManager;
import android.nfc.NfcAdapter;
import android.nfc.NfcManager;
import android.provider.Settings;
import no.tagstory.R;
import no.tagstory.story.GameModeEnum;
import no.tagstory.story.TagTypeEnum;

import java.util.List;

public class PhoneRequirementsUtils {

	/**
	 * Check if current phone supports a storys' game modes.<br>
	 *     Current game modes are: Camera
	 * @param gameModes A list of games modes
	 * @return the first game mode not supported by this phone
	 */
	public static String checkGamemodes(Context context, List<GameModeEnum> gameModes) {
		// For now there are no game modes not supported by this phones
		return null;
	}

	/**
	 * Check if current phones supports the tag types used in a story.<br>
	 * Current tag types are: QR, NFC, and GPS<br>
	 * @param tagTypes A list of tag types
	 * @return the first tag type not supported by this phone
	 */
	public static TagTypeEnum checkTagtypes(Context context, List<TagTypeEnum> tagTypes) {
		for (TagTypeEnum tagType : tagTypes) {
			if (tagType.isQR()) {
				if (!isBarcodeScannerAvailable(context)) return TagTypeEnum.QR;
			} else if (tagType.isNFC()) {
				if (!isNFCEnabled(context)) return TagTypeEnum.NFC;
			} else if (tagType.isGPS()) {
				if (!isGPSEnabled(context)) return TagTypeEnum.GPS;
			} else if (tagType.isBLE()) {
				// not implemented yet
			}
		}

		return null;
	}

	private static boolean isBarcodeScannerAvailable(Context context) {
		PackageManager pm = context.getPackageManager();
		try {
			pm.getPackageInfo(context.getString(R.string.barcode_scanner_play_store), PackageManager.GET_ACTIVITIES);
			return true;
		} catch (PackageManager.NameNotFoundException e) {
			return false;
		}
	}

	public static boolean isNFCEnabled(Context context) {
		NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
		NfcAdapter adapter = manager.getDefaultAdapter();
		return adapter != null
				&& adapter.isEnabled();
	}

	public static boolean isGPSEnabled(Context context) {
		String provider = Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED);
		return provider != null
				&& provider.contains("gps");
	}

	public static boolean hasGPS(Context context) {
		return Settings.Secure.getString(context.getContentResolver(),
				Settings.Secure.LOCATION_PROVIDERS_ALLOWED) != null;
	}

	public static boolean hasNFC(Context context) {
		NfcManager manager = (NfcManager) context.getSystemService(Context.NFC_SERVICE);
		return manager.getDefaultAdapter() != null;
	}
}
