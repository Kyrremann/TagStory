package no.tagstory.story;

public enum HintMethodEnum {

	TEXT("text"),
	IMAGE("image"),
	SOUND("sound"),
	MAP("map"),
	ARROW("arrow");

	private String hint;


	private HintMethodEnum(String hint) {
		this.hint = hint;
	}

	public boolean isImage() {
		return this.equals(IMAGE);
	}

	public boolean isSound() {
		return this.equals(SOUND);
	}

	public boolean isArrow() {
		return this.equals(ARROW);
	}

	public boolean isMap() {
		return this.equals(MAP);
	}
}
