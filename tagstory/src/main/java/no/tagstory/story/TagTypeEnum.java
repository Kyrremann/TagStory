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
				enums.add(TagTypeEnum.fromString(type));
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

	public static TagTypeEnum fromString(String tagType) {
		if (tagType != null) {
			for (TagTypeEnum anEnum : TagTypeEnum.values()) {
				if (tagType.equalsIgnoreCase(anEnum.type)) {
					return anEnum;
				}
			}
		}

		throw new IllegalArgumentException("No constant with text " + tagType + " found");
	}
}
