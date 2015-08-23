package de.fhkl.dbsm.zvafzwects.model.db;

import java.awt.image.BufferedImage;

public class DBCover {
	private int albumId;
	private BufferedImage cover;

	public DBCover() {
		super();
	}

	public DBCover(BufferedImage cover) {
		super();
		this.cover = cover;
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public BufferedImage getCover() {
		return cover;
	}

	public void setCover(BufferedImage cover) {
		this.cover = cover;
	}

}
