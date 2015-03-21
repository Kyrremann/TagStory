package no.tagstory.statistics;

import android.content.Context;
import no.tagstory.R;
import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;
import no.tagstory.story.activity.StoryActivity;
import no.tagstory.utils.Database;

public class StoryHistory {

	private Story story;
	private HistoryNode root;
	private HistoryNode current;
	private int size;

	public void startStory(Story story) {
		this.story = story;
		current = new HistoryNode(story.getStartTag().getUUID());
		root = current;
		size++;
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

	/**
	 * @return true if current has a next
	 */
	public boolean hasNext() {
		if (current == null) {
			current = root;
		}
		return current.hasNext();
	}

	/**
	 * @return true if current has a previous
	 */
	public boolean hasPrevious() {
		if (current == null) {
			current = root;
		}
		return current.hasPrevious();
	}

	public HistoryNode getRootNode() {
		return root;
	}

	public Story getStory() {
		return story;
	}

	public String getNextStory() {
		return current.next != null ? current.next.getTagUUID()
				: null;
	}

	public String getPreviousStory() {
		return current.previous != null ? current.previous.getTagUUID()
				: null;
	}

	public int getSize() {
		return size;
	}

	public void saveToDatabase(Context context, int statisticsId) {
		Database database = new Database(context);
		database.open();
		HistoryNode current = root;
		do {
			database.insertHistory(statisticsId, current);
			current = current.next;
		} while (current != null);
		database.close();
	}
}
