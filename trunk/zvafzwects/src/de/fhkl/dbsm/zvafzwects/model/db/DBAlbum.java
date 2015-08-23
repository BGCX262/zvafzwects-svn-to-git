package de.fhkl.dbsm.zvafzwects.model.db;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 3
 */
public class DBAlbum {
	private int albumId;
	private String title;
	private String interpreter;
	private int numberOfCds;
	private BigDecimal price;
	private String label;
	private int stock;
	private int releaseYear;
	private DBCover cover;
	private DBCategory category;
	private SortedMap<Integer, DBCD> cds;
	private Set<DBCatchword> catchwords;

	public DBAlbum() {
		super();
		this.albumId = -1;
		this.cover = new DBCover();
		this.category = new DBCategory();
		this.price = new BigDecimal(0.00).setScale(2, RoundingMode.HALF_UP);
		this.cds = new TreeMap<Integer, DBCD>();
		this.catchwords = new LinkedHashSet<DBCatchword>();
	}

	public DBAlbum(String title, String interpreter, BigDecimal price,
			String label, int stock, DBCover cover, DBCategory category) {
		super();
		this.title = title;
		this.interpreter = interpreter;
		setPrice(price);
		this.label = label;
		this.stock = stock;
		this.cover = cover;
		this.category = category;
		this.cds = new TreeMap<Integer, DBCD>();
		this.catchwords = new LinkedHashSet<DBCatchword>();
	}

	public int getAlbumId() {
		return albumId;
	}

	public void setAlbumId(int albumId) {
		this.albumId = albumId;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getInterpreter() {
		return interpreter;
	}

	public void setInterpreter(String interpreter) {
		this.interpreter = interpreter;
	}

	public int getNumberOfCds() {
		return numberOfCds;
	}

	public void setNumberOfCcds(int numberOfCds) {
		this.numberOfCds = numberOfCds;
	}

	public BigDecimal getPrice() {
		return price;
	}

	public String getPriceAsString() {
		return price.toPlainString();
	}

	public void setPrice(BigDecimal price) {
		this.price = BigDecimal.valueOf(price.doubleValue()).setScale(2,
				RoundingMode.HALF_UP);
	}

	public String getLabel() {
		return label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public int getStock() {
		return stock;
	}

	public void setStock(int stock) {
		this.stock = stock;
	}

	public int getReleaseYear() {
		return releaseYear;
	}

	public void setReleaseYear(int releaseYear) {
		this.releaseYear = releaseYear;
	}

	public DBCover getCover() {
		return cover;
	}

	public void setCover(DBCover cover) {
		if (cover != null) {
			cover.setAlbumId(this.albumId);
		}
		this.cover = cover;
	}

	public DBCategory getCategory() {
		return category;
	}

	/**
	 * sets the category of an album. if you want to unset the category the
	 * parameter must be null
	 * 
	 * @param category the new category for the album. null to unset the
	 *            category of the album
	 */
	public void setCategory(DBCategory category) {
		// remove the album from the old category
		if (this.category != null)
			this.category.removeAlbum(this);
		// add the album to the new category
		if (category != null) {
			// set the new category
			this.category = category;
			category.addAlbum(this);
		} else {
			this.category = null;
		}
	}

	public Collection<DBCD> getCds() {
		return this.cds.values();
	}

	@Deprecated
	public void setCds(SortedMap<Integer, DBCD> cds) {
		this.cds = cds;
	}

	/**
	 * @param cdNumber the cd number to look for
	 * @return the cd found with the associated key, or null if no cd was found
	 */
	public DBCD getCd(int cdNumber) {
		return this.cds.get(cdNumber);
	}

	protected void addCd(DBCD cd) {
		if (this.cds.put(cd.getCdNumber(), cd) == null)
			this.numberOfCds++;
	}

	protected void removeCd(int cdNumber) {
		if (this.cds.remove(cdNumber) != null)
			this.numberOfCds--;
	}

	public Set<DBCatchword> getCatchwords() {
		return catchwords;
	}

	public void setCatchwords(Set<DBCatchword> catchwords) {
		// remove the album from the old catchwords
		if (!this.catchwords.isEmpty()) {
			for (DBCatchword cw : this.catchwords) {
				cw.removeAlbum(this);
			}
		}
		// set the catchword list
		this.catchwords = catchwords;
		// add the album to the new catchwords
		for (DBCatchword cw : catchwords) {
			cw.addAlbum(this);
		}
	}
}
