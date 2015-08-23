package de.fhkl.dbsm.zvafzwects.model.db;

/**
 * @author Andreas Baur, Jochen Pätzold
 */
public class DBAudioFile {

	private DBTrack track;
	private byte[] audioFile;

	public DBAudioFile() {
		super();
	}

	public DBAudioFile(byte[] audioFile) {
		super();
		this.audioFile = audioFile;
	}

	public DBTrack getTrack() {
		return track;
	}

	public void setTrack(DBTrack track) {
		this.track = track;
	}

	public byte[] getAudioFile() {
		return audioFile;
	}

	public void setAudioFile(byte[] audioFile) {
		this.audioFile = audioFile;
	}

}
