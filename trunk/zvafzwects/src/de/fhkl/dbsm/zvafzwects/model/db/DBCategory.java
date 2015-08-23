package de.fhkl.dbsm.zvafzwects.model.db;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Andreas Baur, Jochen Pätzold
 */
public class DBCategory {
	private int categoryId;
	private String categoryName;
	private List<DBAlbum> alba;

	public DBCategory() {
		super();
		this.alba = new ArrayList<DBAlbum>();
	}

	public DBCategory(String categoryName) {
		super();
		this.categoryName = categoryName;
		this.alba = new ArrayList<DBAlbum>();
	}

	public int getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(int categoryId) {
		this.categoryId = categoryId;
	}
	
	public String getCategoryName() {
		return categoryName;
	}

	public void setCategoryName(String categoryName) {
		this.categoryName = categoryName;
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