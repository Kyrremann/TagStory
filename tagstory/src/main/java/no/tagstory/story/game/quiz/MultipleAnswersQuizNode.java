package no.tagstory.story.game.quiz;

import org.json.JSONArray;

import java.util.List;

public class MultipleAnswersQuizNode implements QuizNodeInterface {

	public static final long serialVersionID = 8577629238L;

	private final String question;
	private final String answer;
	private final List<String> answers;
	private final String correction;
	private final boolean shortQuiz;

	public MultipleAnswersQuizNode(String question, String answer, List<String> answers, String correction, boolean shortQuiz) {
		this.question = question;
		this.answer = answer;
		this.answers = answers;
		this.correction = correction;
		this.shortQuiz = shortQuiz;
	}

	public String getQuestion() {
		return question;
	}

	public boolean isCorrect(String answer) {
		return this.answer.equals(answer);
	}

	public List<String> getAnswers() {
		return answers;
	}

	public String getCorrection() {
		return correction;
	}

	public boolean isShortQuiz() {
		return shortQuiz;
	}
}
