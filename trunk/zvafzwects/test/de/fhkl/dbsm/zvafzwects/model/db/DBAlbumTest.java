package de.fhkl.dbsm.zvafzwects.model.db;

import java.math.BigDecimal;
import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.DBAlbum;
import de.fhkl.dbsm.zvafzwects.model.db.DBCD;
import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;
import de.fhkl.dbsm.zvafzwects.model.db.DBCover;

/**
 * @author Andreas Baur
 * @version 2
 */
public class DBAlbumTest {

	private static DBAlbum album;

	private final static int INIT_ALBUMID = 1;
	private final static String INIT_TITLE = "Test Album";
	private final static String INIT_INTERPRETER = "Test Interpreter";
	private final static BigDecimal INIT_PRICE = new BigDecimal(3.14);
	private final static String INIT_LABEL = "Test Label";
	private final static int INIT_STOCK = 42;
	private final static DBCover INIT_COVER = new DBCover();
	private final static DBCategory INIT_CATEGORY = new DBCategory();

	private static DBCD INIT_CD = new DBCD();

	@BeforeClass
	public static void setUpBeforeClass() {
		album = new DBAlbum(INIT_TITLE, INIT_INTERPRETER, INIT_PRICE,
				INIT_LABEL, INIT_STOCK, INIT_COVER, INIT_CATEGORY);
		INIT_CD.setCdNumber(1);
	}

	@Test
	public void setId() {
		album.setAlbumId(INIT_ALBUMID);
		Assert.assertTrue("setId", INIT_ALBUMID == album.getAlbumId());
	}

	@Test
	public void addCd() {
		album.addCd(INIT_CD);
		Assert.assertEquals("addCd", INIT_CD,
				album.getCd(INIT_CD.getCdNumber()));
		Assert.assertTrue("cdCount", album.getNumberOfCds() == 1);
	}

	@Test
	public void deleteCd() {
		album.addCd(INIT_CD);
		album.removeCd(INIT_CD.getCdNumber());
		Assert.assertTrue("cdCount", album.getNumberOfCds() == 0);
	}
}
