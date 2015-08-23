package de.fhkl.dbsm.zvafzwects.model.db;

import java.util.Collection;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 */
public class DBCD {
	private DBAlbum album;
	private int cdNumber;
	private int numberOfTracks;
	private SortedMap<Integer, DBTrack> tracks;

	public DBCD() {
		super();
		this.tracks = new TreeMap<Integer, DBTrack>();
	}

	public DBCD(DBAlbum album, int cdNumber) {
		super();
		this.album = album;
		this.cdNumber = cdNumber;
		this.tracks = new TreeMap<Integer, DBTrack>();
		album.addCd(this);
	}

	public DBAlbum getAlbum() {
		return album;
	}

	public void setAlbum(DBAlbum album) {
		this.album = album;
	}

	public int getCdNumber() {
		return cdNumber;
	}

	public void setCdNumber(int cdNumber) {
		this.cdNumber = cdNumber;
	}

	public int getNumberOfTracks() {
		return numberOfTracks;
	}

	public void setNumberOfTracks(int numberOfTracks) {
		this.numberOfTracks = numberOfTracks;
	}

	public Collection<DBTrack> getTracks() {
		return tracks.values();
	}

	@Deprecated
	public void setTracks(SortedMap<Integer, DBTrack> tracks) {
		this.tracks = tracks;
	}

	/**
	 * @param trackNumber the track number to look for
	 * @return the track associated with the key or null, if no track found with
	 *         the key
	 */
	public DBTrack getTrack(int trackNumber) {
		return this.tracks.get(trackNumber);
	}

	protected void addTrack(DBTrack track) {
		if (this.tracks.put(track.getTrackNumber(), track) == null)
			this.numberOfTracks++;
	}

	protected void removeTrack(int trackNumber) {
		if (this.tracks.remove(trackNumber) != null)
			this.numberOfTracks--;
	}
}
