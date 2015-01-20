package no.tagstory.statistics;

import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;

import java.util.LinkedList;

public class StoryHistory {

    private Story story;
    private HistoryNode root;
    private HistoryNode current;

    public void startStory(Story story) {
        this.story = story;
        current = new HistoryNode(story.getStartTag());
        root = current;
    }

    public void push(StoryTag tag) {
        HistoryNode temp = current;
        current = new HistoryNode(tag);
        temp.next = current;
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
        return current.hasNext();
    }

    public StoryTag getNextStory() {
        return current.next.tag;
    }
}
