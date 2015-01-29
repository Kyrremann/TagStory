package no.tagstory.story;

public enum StoryStatusEnum {

	DRAFT("status"),
	PENDING("pending"),
	PUBLISHED("published");

	private String status;


	StoryStatusEnum(String status) {
		this.status = status;
	}
}
