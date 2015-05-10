package no.tagstory.statistics;

import android.content.Context;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.utils.Database;

public class StoryHistory {

	private String storyId;
	private HistoryNode root;
	private HistoryNode current;
	private int size;

	public void startStory(Story story) {
		this.storyId = story.getUUID();
		current = new HistoryNode(story.getStartTag().getUUID());
		current.root = true;
		root = current;
		size = 0;
		size++;
	}

	public void resumeStory(String storyId, HistoryNode root, int size) {
		this.storyId = storyId;
		this.root = root;
		this.size = size;
		current = this.root;
	}

	public void push(StoryTag tag) {
		HistoryNode temp = current;
		current = new HistoryNode(tag.getUUID());
		temp.next = current;
		current.previous = temp;
		size++;
	}

	public boolean previous() {
		if (current.hasPrevious()) {
			current = current.previous;
			return true;
		}

		return false;
	}

	public boolean next() {
		if (current.hasNext()) {
			current = current.next;
			return true;
		}

		return false;
	}

	public boolean hasNext() {
		if (current == null) {
			current = root;
		}
		return current.hasNext();
	}

	public boolean hasPrevious() {
		if (current == null) {
			current = root;
		}
		return current.hasPrevious();
	}

	public HistoryNode getRootNode() {
		return root;
	}

	public String getStoryId() {
		return storyId;
	}

	public String getNextStoryId() {
		return current.next != null ? current.next.getTagUUID() : null;
	}

	public String getPreviousStoryId() {
		return current.previous != null ? current.previous.getTagUUID() : null;
	}

	public int getSize() {
		return size;
	}

	public void setFinishedGame(boolean finished) {
		current.finishedGame = finished;
	}

	public boolean hasFinishedGame() {
		return current.finishedGame;
	}

	public void saveToDatabase(Context context, int statisticsId) {
		Database database = new Database(context);
		database.open();
		HistoryNode current = root;
		do {
			// TODO Avoid inserting historyNodes already saved!
			database.insertHistory(statisticsId, current);
			current = current.next;
		} while (current != null);
		database.close();
	}
}
