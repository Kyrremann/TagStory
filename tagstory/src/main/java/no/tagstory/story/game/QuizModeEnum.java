package no.tagstory.story.game;

/**
 * Created by kkh on 24.03.2015.
 */
public enum QuizModeEnum {
	SINGLEQUIZ("true-false"),
	MULTIQUIZSHORT("multiquiz-short"),
	MULTIQUIZLONG("multiquiz-long");


	private String status;

	QuizModeEnum(String status) {
		this.status = status;
	}

	public static QuizModeEnum fromString(String status) {
		if (status != null) {
			for (QuizModeEnum quizModeEnum : QuizModeEnum.values()) {
				if (status.equalsIgnoreCase(quizModeEnum.status)) {
					return quizModeEnum;
				}
			}
			return null;
		}
		throw new IllegalArgumentException("No constant with text " + status + " found");
	}
}
