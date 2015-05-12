package no.tagstory.statistics;

public class Location extends android.location.Location {

	private int id = -1;

	public Location(String provider) {
		super(provider);
	}

	public Location(android.location.Location l) {
		super(l);
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isSaved() {
		return id != -1;
	}
}
