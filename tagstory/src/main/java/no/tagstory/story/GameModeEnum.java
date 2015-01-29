package no.tagstory.story;

import java.util.ArrayList;
import java.util.List;

public enum GameModeEnum {

	NONE("none"),
	CAMERA("camera"),
	QUIZ("quiz");

	private String mode;


	private GameModeEnum(String mode) {
		this.mode = mode;
	}

	public boolean isQuiz() {
		return this.equals(QUIZ);
	}

	public boolean isCamera() {
		return this.equals(CAMERA);
	}


	public static List<GameModeEnum> convert(List<String> types) {
		List<GameModeEnum> enums = new ArrayList<GameModeEnum>(types.size());
		for (String type : types) {
			try {
				enums.add(GameModeEnum.fromString(type));
			} catch (IllegalArgumentException e) {}
		}

		return enums;
	}

	public static GameModeEnum fromString(String mode) {
		if (mode != null) {
			for (GameModeEnum anEnum : GameModeEnum.values()) {
				if (mode.equalsIgnoreCase(anEnum.mode)) {
					return anEnum;
				}
			}
		}

		throw new IllegalArgumentException("No constant with text " + mode + " found");
	}
}
