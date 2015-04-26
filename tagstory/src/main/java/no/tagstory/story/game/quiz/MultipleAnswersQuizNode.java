package no.tagstory.story.game.quiz;

import java.util.List;

public class MultipleAnswersQuizNode implements QuizNodeInterface {

	public static final long serialVersionID = 8577629238L;

	private final String question;
	private final String answer;
	private final String[] answers;
	private final String correction;

	public MultipleAnswersQuizNode(String question, String answer, List<String> answers, String correction) {
		this.question = question;
		this.answer = answer;
		this.answers = answers.toArray(new String[answers.size()]);
		this.correction = correction;
	}

	@Override
	public boolean hasCorrection() {
		return false;
	}

	public String getQuestion() {
		return question;
	}

	public boolean isCorrect(String answer) {
		return this.answer.equals(answer);
	}

	public String[] getAnswers() {
		return answers;
	}

	public String getCorrection() {
		return correction;
	}
}
