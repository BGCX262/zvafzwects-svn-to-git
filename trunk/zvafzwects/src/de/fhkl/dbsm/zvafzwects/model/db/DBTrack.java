package de.fhkl.dbsm.zvafzwects.model.db;

/**
 * @author Andreas Baur, Jochen Pätzold
 */
public class DBTrack {
	private DBCD cd;
	private int trackNumber;
	private String title;
	private long duration;
	private DBAudioFile audioFile;

	public DBTrack() {
		super();
	}

	public DBTrack(DBCD cd, int trackNumber, String title, long duration, DBAudioFile audioFile) {
		super();
		this.cd = cd;
		this.trackNumber = trackNumber;
		this.title = title;
		this.duration = duration;
		this.audioFile = audioFile;
		audioFile.setTrack(this);
		cd.addTrack(this);
	}
	
	public DBCD getCd() {
		return cd;
	}

	public void setCd(DBCD cd) {
		this.cd = cd;
	}

	public int getTrackNumber() {
		return trackNumber;
	}

	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public DBAudioFile getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(DBAudioFile audioFile) {
		this.audioFile = audioFile;
	}

}
