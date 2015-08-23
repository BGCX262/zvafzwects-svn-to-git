package de.fhkl.dbsm.zvafzwects.model.db;

import org.junit.Assert;
import org.junit.BeforeClass;
import org.junit.Test;

import de.fhkl.dbsm.zvafzwects.model.db.DBCategory;

/**
 * @author Andreas Baur, Jochen Pätzold
 * @version 2
 */
public class DBCategoryTest {
	
	private static DBCategory category;
	
	private final static int INIT_CATEGORYID = 1;
	private final static String INIT_CATEGORYNAME = "Test Category";
	
	@BeforeClass
	public static void setUpBeforeClass() {
		category = new DBCategory(INIT_CATEGORYNAME);
	}
	
	@Test
	public void setId() {
		category.setCategoryId(INIT_CATEGORYID);
		Assert.assertTrue("setId", INIT_CATEGORYID == category.getCategoryId());
	}

}
