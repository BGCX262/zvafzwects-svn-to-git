/**
 * 
 */
package de.fhkl.dbsm.zvafzwects.model.adapter;

/**
 * Adapter used in the {@link InputDataAlbum} class to represent a track.
 * 
 * @author Markus Henn
 * 
 */
public class InputDataTrack {
	private int cdNumber;
	private int trackNumber;
	private String title;
	private long duration;
	private byte[] trackBytes;

	/**
	 * @param cdNumber
	 * @param trackNumber
	 * @param title
	 * @param trackBytes
	 * @param duration
	 */
	public InputDataTrack(int cdNumber, int trackNumber, String title,
			long duration, byte[] trackBytes) {
		super();
		this.cdNumber = cdNumber;
		this.trackNumber = trackNumber;
		this.title = title;
		this.duration = duration;
		this.trackBytes = trackBytes;
	}

	/**
	 * @return the cdNumber
	 */
	public int getCdNumber() {
		return cdNumber;
	}

	/**
	 * @param cdNumber
	 *            the cdNumber to set
	 */
	public void setCdNumber(int cdNumber) {
		this.cdNumber = cdNumber;
	}

	/**
	 * @return the trackNumber
	 */
	public int getTrackNumber() {
		return trackNumber;
	}

	/**
	 * @param trackNumber
	 *            the trackNumber to set
	 */
	public void setTrackNumber(int trackNumber) {
		this.trackNumber = trackNumber;
	}

	/**
	 * @return the title
	 */
	public String getTitle() {
		return title;
	}

	/**
	 * @param title
	 *            the title to set
	 */
	public void setTitle(String title) {
		this.title = title;
	}

	/**
	 * @return the duration
	 */
	public long getDuration() {
		return duration;
	}

	/**
	 * @param duration
	 *            the duration to set
	 */
	public void setDuration(long duration) {
		this.duration = duration;
	}

	/**
	 * @return the trackBytes
	 */
	public byte[] getTrackBytes() {
		return trackBytes;
	}

	/**
	 * @param trackBytes
	 *            the trackBytes to set
	 */
	public void setTrackBytes(byte[] trackBytes) {
		this.trackBytes = trackBytes;
	}

}
