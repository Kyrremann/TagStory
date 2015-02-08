package no.tagstory.statistics;

import no.tagstory.story.Story;
import no.tagstory.story.StoryTag;

import java.util.LinkedList;

public class StoryHistory {

    private Story story;
    private HistoryNode root;
    private HistoryNode current;
    private int size;

    public void startStory(Story story) {
        this.story = story;
        current = new HistoryNode(story.getStartTag());
        root = current;
        size++;
    }

    public void push(StoryTag tag) {
        HistoryNode temp = current;
        current = new HistoryNode(tag);
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

    public StoryTag getNextStory() {
        return current.next.tag;
    }

    public StoryTag getPreviousStory() {
        return current.previous.tag;
    }

    public int getSize() {
        return size;
    }
}
