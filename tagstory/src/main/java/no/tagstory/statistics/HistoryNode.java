package no.tagstory.statistics;

import no.tagstory.story.StoryTag;

public class HistoryNode {

    protected StoryTag tag;

    public HistoryNode next;
    public HistoryNode previous;

    public HistoryNode(StoryTag tag) {
        this.tag = tag;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrevious() {
        return previous != null;
    }

	public String getTagUUID() {
		return tag.getUUID();
	}
}
