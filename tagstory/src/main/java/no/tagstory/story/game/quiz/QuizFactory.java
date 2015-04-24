package no.tagstory.story.game.quiz;

import no.tagstory.utils.StoryParser;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class QuizFactory {

	public static TrueFalseQuizNode createTrueFalseQuiz(JSONObject jsonQuestion) throws JSONException {
		return new TrueFalseQuizNode(jsonQuestion.getString(StoryParser.QUIZ_QUESTION),
				jsonQuestion.getBoolean(StoryParser.QUIZ_ANSWER),
				jsonQuestion.optString(StoryParser.QUIZ_CORRECTION));
	}

	public static MultipleAnswersQuizNode createMultiquizLong(JSONObject jsonQuestion) throws JSONException {
		return new MultipleAnswersQuizNode(jsonQuestion.getString(StoryParser.QUIZ_QUESTION),
				jsonQuestion.getString(StoryParser.QUIZ_ANSWER),
				generateList(jsonQuestion.getJSONArray(StoryParser.QUIZ_ANSWERS)),
				jsonQuestion.optString(StoryParser.QUIZ_CORRECTION),
				true);
	}

	public static MultipleAnswersQuizNode createMultiquizShort(JSONObject jsonQuestion) throws JSONException {
		return new MultipleAnswersQuizNode(jsonQuestion.getString(StoryParser.QUIZ_QUESTION),
				jsonQuestion.getString(StoryParser.QUIZ_ANSWER),
				generateList(jsonQuestion.getJSONArray(StoryParser.QUIZ_ANSWERS)),
				jsonQuestion.optString(StoryParser.QUIZ_CORRECTION),
				false);
	}

	private static List<String> generateList(JSONArray jsonArray) {
		List<String> list = new ArrayList<>();
		for (int i = 0; i < jsonArray.length(); i++) {
			String answer = "";
			try {
				answer = jsonArray.getString(i);
			} catch (JSONException e) {
			}
			list.add(answer);
		}
		return list;
	}
}
