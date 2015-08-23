package de.fhkl.dbsm.zvafzwects.model.db;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Baur, Jochen Pätzold
 */
public class DBCatchword {
	private int catchwordId;
	private String catchwordName;
	private List<DBAlbum> alba;

	public DBCatchword() {
		super();
		this.alba = new ArrayList<DBAlbum>();
	}

	public DBCatchword(String catchwordName) {
		super();
		this.catchwordName = catchwordName;
		this.alba = new ArrayList<DBAlbum>();
	}

	public int getCatchwordId() {
		return catchwordId;
	}

	public void setCatchwordId(int catchwordId) {
		this.catchwordId = catchwordId;
	}

	public String getCatchwordName() {
		return catchwordName;
	}

	public void setCatchwordName(String catchwordName) {
		this.catchwordName = catchwordName;
	}

	public List<DBAlbum> getAlba() {
		return alba;
	}

	public void setAlba(List<DBAlbum> alba) {
		this.alba = alba;
	}

	public void addAlbum(DBAlbum album) {
		this.alba.add(album);
	}

	public void removeAlbum(DBAlbum album) {
		this.alba.remove(album);
	}
}
