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

import de.lokalhorst.db.dto.UnitDTO;

/**
 * @author Jochen Pätzold
 * 
 */
public class UnitDBTest
{
    private static Connection cn = null;
    private final int UNIT_ID = 314159265;
    private final String UNIT_NAME = "JUn";

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
     * 
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
	final String insert_query = "INSERT INTO Unit VALUES(?, ?)";
	PreparedStatement ps = cn.prepareStatement(insert_query);
	ps.setInt(1, UNIT_ID);
	ps.setString(2, UNIT_NAME);

	ps.executeUpdate();
    }

    /**
     * @throws java.sql.SQLException
     */
    @After
    public void tearDown() throws SQLException
    {	final String delete_query = "DELETE FROM Unit WHERE unit_id=?";
	PreparedStatement ps = cn.prepareStatement(delete_query);
	ps.setInt(1, UNIT_ID);

	ps.executeUpdate();
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.UnitDB#getUnits(java.sql.Connection)}.
     */
    @Test
    public void testGetUnits()
    {
	LinkedList<UnitDTO> units = UnitDB.getUnits(cn);
	assertFalse("testGetUnits", units.isEmpty());
    }
}
