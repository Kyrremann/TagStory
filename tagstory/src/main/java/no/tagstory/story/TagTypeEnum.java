package no.tagstory.story;

import java.util.ArrayList;
import java.util.List;

public enum TagTypeEnum {

	QR("qr"),
	NFC("nfc"),
	GPS("gps"),
	BLE("ble");

	private String type;

	TagTypeEnum(String type) {
		this.type = type;
	}

	public static List<TagTypeEnum> convert(List<String> types) {
		List<TagTypeEnum> enums = new ArrayList<TagTypeEnum>(types.size());
		for (String type : types) {
			try {
				enums.add(TagTypeEnum.valueOf(type));
			} catch (IllegalArgumentException e) {}
		}

		return enums;
	}

	public boolean isQR() {
		return this.equals(QR);
	}

	public boolean isNFC() {
		return this.equals(NFC);
	}

	public boolean isGPS() {
		return this.equals(GPS);
	}

	public boolean isBLE() {
		return this.equals(BLE);
	}
}
