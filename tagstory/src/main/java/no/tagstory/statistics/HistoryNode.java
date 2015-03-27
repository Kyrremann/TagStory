package no.tagstory.statistics;

public class HistoryNode {

	protected String tagId;

	public HistoryNode next;
	public HistoryNode previous;
	public boolean root = false;

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
