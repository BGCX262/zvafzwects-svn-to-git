package de.fhkl.dbsm.zvafzwects.model.db;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.DBCatchword;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 */
public class DBCatchwordTest {

	private static DBCatchword catchword;

	private final static int INIT_CATCHWORDID = 1;
	private final static String INIT_CATCHWORDNAME = "Test Catchword";

	@BeforeClass
	public static void setUpBeforeClass() {
		catchword = new DBCatchword(INIT_CATCHWORDNAME);
	}

	@Test
	public void setId() {
		catchword.setCatchwordId(INIT_CATCHWORDID);
		Assert.assertTrue("setId",
				INIT_CATCHWORDID == catchword.getCatchwordId());
	}

}
