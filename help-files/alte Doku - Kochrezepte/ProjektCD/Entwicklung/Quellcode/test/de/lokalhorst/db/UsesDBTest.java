/**
 * 
 */
package de.lokalhorst.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import de.lokalhorst.helper.IngredientHelper;

/**
 * @author Jochen Pätzold
 * 
 */
public class UsesDBTest
{
    private static Connection cn = null;
    private final static int RECP_ID = 314159265;
    private final static int INGR_ID = 314159265;
    private final static int UNIT_ID = 314159265;
    private final static int DIFF_ID = 314159265;
    private final static int CAT_ID = 314159265;
    private final static int COOK_ID = 314159265;
    private final int AMMOUNT = 42;

    /**
     * @throws java.sql.SQLException
     */
    @BeforeClass
    public static void setUpBeforeClass() throws SQLException
    {
	cn = ConnectDB.getConnection(false);
	if (null == cn)
	    throw new SQLException();

	// insert difficulty (-> recipe)
	final String insert_difficulty = "INSERT INTO Difficulty VALUES(?, 'JUnit difficulty')";
	PreparedStatement ps = cn.prepareStatement(insert_difficulty);
	ps.setInt(1, DIFF_ID);
	ps.executeUpdate();

	// insert category (-> recipe)
	final String insert_category = "INSERT INTO Category VALUES(?, 'JUnit category')";
	ps = cn.prepareStatement(insert_category);
	ps.setInt(1, CAT_ID);
	ps.executeUpdate();

	// insert cook (-> recipe)
	final String insert_cook = "INSERT INTO Cook VALUES(?, 'JUnit cook', '', false, '', '')";
	ps = cn.prepareStatement(insert_cook);
	ps.setInt(1, COOK_ID);
	ps.executeUpdate();

	// insert recipe (-> uses)
	final String insert_recipe = "INSERT INTO Recipe VALUES(?, 'JUnit recipe', '', ?, ?, ?, 0, false, ?, ?, ?)";
	ps = cn.prepareStatement(insert_recipe);
	Date now = new Date(new java.util.Date().getTime());
	ps.setInt(1, RECP_ID);
	ps.setDate(2, now);
	ps.setDate(3, now);
	ps.setDate(4, now);
	ps.setInt(5, DIFF_ID);
	ps.setInt(6, CAT_ID);
	ps.setInt(7, COOK_ID);
	ps.executeUpdate();

	// insert unit (-> ingredient)
	final String insert_unit = "INSERT INTO Unit VALUES(?, 'JUn')";
	ps = cn.prepareStatement(insert_unit);
	ps.setInt(1, UNIT_ID);
	ps.executeUpdate();

	// insert ingredient (-> uses)
	final String insert_ingredient = "INSERT INTO Ingredient VALUES(?,'JUnit ingredient', ?)";
	ps = cn.prepareStatement(insert_ingredient);
	ps.setInt(1, RECP_ID);
	ps.setInt(2, UNIT_ID);
	ps.executeUpdate();
    }

    /**
     * @throws java.sql.SQLException
     */
    @AfterClass
    public static void tearDownAfterClass() throws SQLException
    {

	// delete recipe
	final String delete_recipe = "DELETE FROM Recipe WHERE recp_id=?";
	PreparedStatement ps = cn.prepareStatement(delete_recipe);
	ps.setInt(1, RECP_ID);
	ps.executeUpdate();

	// delete ingredient
	final String delete_ingredient = "DELETE FROM Ingredient WHERE ingr_id=?";
	ps = cn.prepareStatement(delete_ingredient);
	ps.setInt(1, INGR_ID);
	ps.executeUpdate();

	// delete unit
	final String delete_unit = "DELETE FROM Unit WHERE unit_id=?";
	ps = cn.prepareStatement(delete_unit);
	ps.setInt(1, UNIT_ID);
	ps.executeUpdate();
	ConnectDB.closeConnection(cn);
    }

    /**
     * @throws java.sql.SQLException
     */
    @Before
    public void setUp() throws SQLException
    {
	// insert uses
	final String insert_query = "INSERT INTO Uses VALUES(?, ?, ?)";
	PreparedStatement ps = cn.prepareStatement(insert_query);
	ps.setInt(1, RECP_ID);
	ps.setInt(2, INGR_ID);
	ps.setInt(3, AMMOUNT);
	ps.executeUpdate();
    }

    /**
     * @throws java.sql.SQLException
     */
    @After
    public void tearDown() throws SQLException
    {
	// delete uses
	final String delete_uses = "DELETE FROM Uses WHERE recp_id=? AND ingr_id=?";
	PreparedStatement ps = cn.prepareStatement(delete_uses);
	ps.setInt(1, RECP_ID);
	ps.setInt(2, INGR_ID);
	ps.executeUpdate();
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.UsesDB#saveChanges(java.sql.Connection, int, java.util.Collection)}
     * .
     * 
     * @throws SQLException
     */
    @Test
    public void testSaveChanges() throws SQLException
    {
	Collection<IngredientHelper> ingredients = new LinkedList<IngredientHelper>();
	IngredientHelper ingredient = new IngredientHelper(INGR_ID, "?", 5,
		UNIT_ID, "?");
	ingredients.add(ingredient);
	int changes = UsesDB.saveChanges(cn, RECP_ID, ingredients);
	assertTrue("testSaveChanges", 1 == changes);
    }
    
    @Test (expected = SQLException.class)
    public void testExceptionSaveChanges() throws SQLException {
	Collection<IngredientHelper> ingredients = new LinkedList<IngredientHelper>();
	IngredientHelper ingredient = new IngredientHelper(INGR_ID, "?", 5,
		UNIT_ID, "?");
	ingredients.add(ingredient);
	UsesDB.saveChanges(cn, -42, ingredients);
    }

}
