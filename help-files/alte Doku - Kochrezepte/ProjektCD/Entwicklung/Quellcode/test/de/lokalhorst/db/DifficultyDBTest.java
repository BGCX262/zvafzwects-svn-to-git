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

import de.lokalhorst.db.dto.DifficultyDTO;

/**
 * @author Jochen Pätzold
 * 
 */
public class DifficultyDBTest
{
    private static Connection cn = null;
    private final int DIFF_LEVEL = 314159265;
    private final String DESCRIPTION = "TestDifficulty";

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
	final String insert_query = "INSERT INTO Difficulty VALUES(?, ?)";
	PreparedStatement ps = cn.prepareStatement(insert_query);
	ps.setInt(1, DIFF_LEVEL);
	ps.setString(2, DESCRIPTION);

	ps.executeUpdate();
    }

    /**
     * @throws java.sql.SQLException
     */
    @After
    public void tearDown() throws SQLException
    {
	final String delete_query = "DELETE FROM Difficulty WHERE diff_level=?";
	PreparedStatement statement = cn.prepareStatement(delete_query);
	statement.setInt(1, DIFF_LEVEL);

	statement.executeUpdate();
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.DifficultyDB#getDifficulties(java.sql.Connection)}
     * .
     */
    @Test
    public void testGetDifficulties()
    {
	LinkedList<DifficultyDTO> difftList = DifficultyDB.getDifficulties(cn);
	assertFalse("testGetDifficulties", difftList.isEmpty());
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.DifficultyDB#getDescriptionByLevel(java.sql.Connection, int)}
     * .
     */
    @Test
    public void testGetDescriptionByLevel()
    {
	int validLevel = DIFF_LEVEL;
	String diff = DifficultyDB.getDescriptionByLevel(cn, validLevel);
	assertTrue("testNameById (pos)", diff.equals(DESCRIPTION));

	int invalidLevel = -42;
	diff = DifficultyDB.getDescriptionByLevel(cn, invalidLevel);
	assertTrue("testNameById (neg)", null == diff);
    }

}
