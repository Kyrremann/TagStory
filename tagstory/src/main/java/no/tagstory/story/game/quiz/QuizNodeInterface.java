package no.tagstory.story.game.quiz;

import java.io.Serializable;

public interface QuizNodeInterface extends Serializable {

	boolean isCorrect(String answer);

	boolean hasCorrection();

	String getQuestion();
}
