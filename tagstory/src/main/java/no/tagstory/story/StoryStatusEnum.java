package no.tagstory.story;

public enum StoryStatusEnum {

	DRAFT("draft"),
	PENDING("pending"),
	PUBLISHED("published");

	private String status;


	StoryStatusEnum(String status) {
		this.status = status;
	}

	public static StoryStatusEnum fromString(String status) {
		if (status != null) {
			for (StoryStatusEnum anEnum : StoryStatusEnum.values()) {
				if (status.equalsIgnoreCase(anEnum.status)) {
					return anEnum;
				}
			}
		}

		throw new IllegalArgumentException("No constant with text " + status + " found");
	}
}
