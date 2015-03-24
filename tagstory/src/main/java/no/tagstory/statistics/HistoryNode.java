package no.tagstory.statistics;

import no.tagstory.story.StoryTag;

public class HistoryNode {

	protected String tagId;

	public HistoryNode next;
	public HistoryNode previous;

	public HistoryNode(String tagId) {
		this.tagId = tagId;
	}

	public boolean hasNext() {
		return next != null;
	}

	public boolean hasPrevious() {
		return previous != null;
	}

	public String getTagUUID() {
		return tagId;
	}
}
