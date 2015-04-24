package no.tagstory.story.game.quiz;

public enum QuizTypeEnum {

	TRUEFALSEQUIZ("true-false"),
	MULTIQUIZSHORT("multiquiz-short"),
	MULTIQUIZLONG("multiquiz-long");


	private String description;

	QuizTypeEnum(String description) {
		this.description = description;
	}

	public static QuizTypeEnum fromString(String description) {
		if (description != null) {
			for (QuizTypeEnum quizTypeEnum : QuizTypeEnum.values()) {
				if (description.equalsIgnoreCase(quizTypeEnum.description)) {
					return quizTypeEnum;
				}
			}
		}

		throw new IllegalArgumentException("No constant with text " + description + " found");
	}
}
