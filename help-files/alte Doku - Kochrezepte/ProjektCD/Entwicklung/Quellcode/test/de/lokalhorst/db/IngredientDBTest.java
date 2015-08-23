/**
 * 
 */
package de.lokalhorst.db;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;
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
public class IngredientDBTest
{
    private static Connection cn = null;
    private final static int INGR_ID = 314159265;
    private final static String INGR_NAME = "JUnit ingredient";
    private final static int UNIT_ID = 314159265;
    private final static String UNIT_NAME = "JUn";
    private static final int DIFF_ID = 314159265;
    private static final int CAT_ID = 314159265;
    private static final int COOK_ID = 314159265;
    private static final int RECP_ID = 314159265;
    private static final int AMMOUNT = 13;

    /**
     * @throws java.sql.SQLException
     */
    @BeforeClass
    public static void setUpBeforeClass() throws SQLException
    {
	cn = ConnectDB.getConnection();
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

	// insert recipe
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
	final String insert_unit = "INSERT INTO Unit VALUES(?, ?)";
	ps = cn.prepareStatement(insert_unit);
	ps.setInt(1, UNIT_ID);
	ps.setString(2, UNIT_NAME);

	ps.executeUpdate();
    }

    /**
     * @throws java.sql.SQLException
     */
    @AfterClass
    public static void tearDownAfterClass() throws java.sql.SQLException
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
	final String insert_query = "INSERT INTO Ingredient VALUES(?, ?, ?)";
	PreparedStatement ps = cn.prepareStatement(insert_query);
	ps.setInt(1, INGR_ID);
	ps.setString(2, INGR_NAME);
	ps.setInt(3, UNIT_ID);

	ps.executeUpdate();
    }

    /**
     * @throws java.sql.SQLException
     */
    @After
    public void tearDown() throws SQLException
    {
	final String delete_query = "DELETE FROM Ingredient WHERE ingr_id=?";
	PreparedStatement statement = cn.prepareStatement(delete_query);
	statement.setInt(1, INGR_ID);

	statement.executeUpdate();
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.IngredientDB#getIngredientsByRecipeID(java.sql.Connection, int)}
     * .
     * 
     * @throws SQLException
     */
    @Test
    public void testGetIngredientsByRecipeID() throws SQLException
    { // insert uses
	final String insert_query = "INSERT INTO Uses VALUES(?, ?, ?)";
	PreparedStatement ps = cn.prepareStatement(insert_query);
	ps.setInt(1, RECP_ID);
	ps.setInt(2, INGR_ID);
	ps.setInt(3, AMMOUNT);
	ps.executeUpdate();
	
	LinkedList<IngredientHelper> ingredients = (LinkedList<IngredientHelper>) IngredientDB
		.getIngredientsByRecipeID(cn, RECP_ID);
	assertFalse("testGetIngredientsByRecipeID", ingredients.isEmpty());

	// delete uses
	final String delete_uses = "DELETE FROM Uses WHERE recp_id=? AND ingr_id=?";
	ps = cn.prepareStatement(delete_uses);
	ps.setInt(1, RECP_ID);
	ps.setInt(2, INGR_ID);
	ps.executeUpdate();
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.IngredientDB#getIngredients(java.sql.Connection)}
     * .
     */
    @Test
    public void testGetIngredients()
    {

	LinkedList<IngredientHelper> ingredients = IngredientDB
		.getIngredients(cn);
	assertFalse("testGetIngredients", ingredients.isEmpty());

    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.IngredientDB#existsName(java.sql.Connection, java.lang.String)}
     * .
     */
    @Test
    public void testExistsName()
    {
	assertTrue("testExistsName", IngredientDB.existsName(cn, INGR_NAME));
    }

    /**
     * Test method for
     * {@link de.lokalhorst.db.IngredientDB#createIngredient(java.sql.Connection, java.lang.String, int)}
     * .
     * 
     * @throws SQLException
     */
    @Test
    public void testCreateIngredient() throws SQLException
    {
	assertTrue("testCreateIngredient", IngredientDB.createIngredient(cn,
		"JUnit CreateIngredient", UNIT_ID));
	// IngridientName und UnitID sind primary key
	assertFalse("testCreateIngredient", IngredientDB.createIngredient(cn,
		"JUnit CreateIngredient", UNIT_ID));

	// und wieder löschen
	final String delete_query = "DELETE FROM Ingredient WHERE ingr_name=? AND ingr_unit=?";
	PreparedStatement ps = cn.prepareStatement(delete_query);
	ps.setString(1, "JUnit CreateIngredient");
	ps.setInt(2, UNIT_ID);
	ps.executeUpdate();
    }

}
