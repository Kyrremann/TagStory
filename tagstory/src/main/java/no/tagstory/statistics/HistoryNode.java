package no.tagstory.statistics;

public class HistoryNode {

	public int id = -1;
	protected String tagId;

	public HistoryNode next;
	public HistoryNode previous;
	public boolean root = false;
    public boolean finishedGame;

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

	public boolean isSaved() {
		return id != -1;
	}
}
