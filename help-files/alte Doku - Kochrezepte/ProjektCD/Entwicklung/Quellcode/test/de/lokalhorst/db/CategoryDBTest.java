/**
 * 
 */
package de.lokalhorst.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.lokalhorst.db.dto.CategoryDTO;

/**
 * @author Jochen Pätzold
 */
public class CategoryDBTest
{
    private static Connection cn = null;
    private final int CAT_ID = 314159265;
    private final String CAT_NAME = "TestCategory";

    /**
     * @throws java.sql.SQLException
     */
    @BeforeClass
    public static void setUpBeforeClass() throws SQLException
    {
	cn = ConnectDB.getConnection();
	if (null == cn)
	    throw new SQLException();
    }

    /**
     * @throws java.lang.Exception
     */
    @AfterClass
    public static void tearDownAfterClass()
    {
	ConnectDB.closeConnection(cn);
    }

    /**
     * @throws java.sql.SQLException
     */
    @Before
    public void setUp() throws SQLException
    {
	final String insert_query = "INSERT INTO Category VALUES(?, ?)";
	PreparedStatement ps = cn.prepareStatement(insert_query);
	ps.setInt(1, CAT_ID);
	ps.setString(2, CAT_NAME);

	ps.executeUpdate();
    }

    /**
     * @throws java.sql.SQLException
     */
    @After
    public void tearDown() throws SQLException
    {
	final String delete_query = "DELETE FROM Category WHERE cat_id=?";
	PreparedStatement statement = cn.prepareStatement(delete_query);
	statement.setInt(1, CAT_ID);

	statement.executeUpdate();
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.CategoryDB#NameById(java.sql.Connection, int)}.
     */
    @Test
    public void testNameById()
    {
	int validID = CAT_ID;
	String cat = CategoryDB.NameById(cn, validID);
	assertTrue("testNameById (pos)", cat.equals(CAT_NAME));
	
	int invalidID = -42;
	cat = CategoryDB.NameById(cn, invalidID);
	assertTrue("testNameById (neg)", null == cat);
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.CategoryDB#getCategories(java.sql.Connection)}.
     */
    @Test
    public void testGetCategories()
    {
	LinkedList<CategoryDTO> catList = CategoryDB.getCategories(cn);
	assertFalse("testGetCategories", catList.isEmpty());
    }
}
