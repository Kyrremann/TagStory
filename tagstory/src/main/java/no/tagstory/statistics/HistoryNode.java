package no.tagstory.statistics;

import no.tagstory.story.StoryTag;

public class HistoryNode {

    protected StoryTag tag;

    public HistoryNode next;
    public HistoryNode previous;
    // TODO Save this variable to the database
    public boolean finishedGame;

    public HistoryNode(StoryTag tag) {
        this.tag = tag;
    }

    public boolean hasNext() {
        return next != null;
    }

    public boolean hasPrevious() {
        return previous != null;
    }
}
