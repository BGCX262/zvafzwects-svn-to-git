package de.lokalhorst.db;

import static org.junit.Assert.*;
import java.util.Collection;
import de.lokalhorst.db.ConnectDB;
import java.sql.Date;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.LinkedList;

import org.junit.*;
import de.lokalhorst.db.dto.PreparationDTO;
import de.lokalhorst.helper.IngredientHelper;
import de.lokalhorst.helper.RecipeHelper;


/**
 * 
 * @author Christoph Antes
 *
 */
public class RecipeDBTest
{
    private static Connection connection = null;
    private static Connection connection2 = null;
    
    private int recp_id = 47111337;
    private String r_name = "testrecipe_junit";
    private String r_name2 = "testrecipe_junit_2";
    private String r_description = "testbeschreibung_junit";
    private String r_description3 = "testbeschreibung_junit_3";
    
    Date now = new Date(new java.util.Date().getTime());
    private Date date_applied = now;
    private Date date_released = now;
    private Date date_last_edited = now;
    
    private int prep_time = 42;
    private boolean is_released = true;
    private int cat_id = 111;
    private String cat_name = "testcategory_junit";
    private int difficulty_id = 2;
    private String difficulty = "schwer";
    private int persons = 4;
    
   
    
    private int ingr_id = 348956738;
    private String ingr_name = "testzutat_junit";
    
    private int unit_id = 99;
    private String unit_name = "te";
    
    int amount = 4711;
    
    private int prep_step = 1;
    private String instruction = "testinstruction_junit";
    
    
    //cook
    private int cook_id = 4657823;
    private String cook_name = "testkoch_junit";
    private boolean is_admin = false;
    private String pwd = "password_junit";
    private String sec_question = "sec_question_junit";
    private String sec_answer = "sec_answer_junit";
    
    
    
    private Collection<IngredientHelper> ingredients = new LinkedList<IngredientHelper>();
    private Collection<PreparationDTO> prep_steps = new LinkedList<PreparationDTO>();
    
    
    //Objekt 1
    private RecipeHelper recipe = new RecipeHelper(recp_id, r_name, r_description, date_applied, date_last_edited, 
	    prep_time, is_released, cat_id, cook_id, difficulty_id, difficulty, cook_name,
	    is_admin, cat_name, persons);
    
    
    //Objekt 2: Unterscheidet sich in einigen Attributen von Obj1, um die Änderungsfunktionen testen zu können
    private String r_description2 = "testbeschreibung_junit_geändert";
    private int prep_time2 = 200;
    private RecipeHelper recipe2 = new RecipeHelper(recp_id, r_name, r_description2, date_applied, date_last_edited, 
	    prep_time2, is_released, cat_id, cook_id, difficulty_id, difficulty, cook_name,
	    is_admin, cat_name, persons);
    

    @BeforeClass
    public static void setUpBeforeClass() throws Exception
    {
	connection = ConnectDB.getConnection();
	if (null == connection)
	{
	    throw new Exception();
	}
	
	connection2 = ConnectDB.getConnection(false);
	if (null == connection2)
	{
	    throw new Exception();
	}
	    
    }

    @AfterClass
    public static void tearDownAfterClass()
    {
	ConnectDB.closeConnection(connection);
	ConnectDB.closeConnection(connection2);
    }

    
    
   
    @Before
    public void setUp() throws Exception
    {
	/*
	 * Es wird vor den Testmethoden ein komplettes Rezept in der Datebank erstellt. Mit allen
	 * Attributen
	 */
	//Testkoch anlegen
	final String insert_query_cook = "INSERT INTO Cook VALUES(?, ?, ?, ?,  ?, ?)";
	PreparedStatement statement7 = connection.prepareStatement(insert_query_cook);
	statement7.setInt(1, cook_id);
	statement7.setString(2, cook_name);
	statement7.setString(3, pwd);
	statement7.setBoolean(4, is_admin);
	statement7.setString(5, sec_question);
	statement7.setString(6, sec_answer);
	statement7.executeUpdate();
	
	//Kategorie
	final String insert_query_cat = "INSERT INTO category values(?, ?)";
	PreparedStatement statement2 = connection.prepareStatement(insert_query_cat);
	statement2.setInt(1, cat_id);
	statement2.setString(2, cat_name);
	statement2.executeUpdate();
	
	//Einheit
	final String insert_query_unit = "INSERT INTO unit values(?, ?)";
	PreparedStatement statement4 = connection.prepareStatement(insert_query_unit);
	statement4.setInt(1, unit_id);
	statement4.setString(2, unit_name);
	statement4.executeUpdate();
	
	
	
	//Zutaten
	final String insert_query_ingr = "INSERT INTO ingredient values(?, ?, ?)";
	PreparedStatement statement3 = connection.prepareStatement(insert_query_ingr);
	statement3.setInt(1, ingr_id);
	statement3.setString(2, ingr_name);
	statement3.setInt(3, unit_id);
	statement3.executeUpdate();

	//Rezept
	final String insert_query_recipe = "INSERT INTO Recipe VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
	PreparedStatement statement = connection.prepareStatement(insert_query_recipe);
	statement.setInt(1, recp_id);
	statement.setString(2, r_name);
	statement.setString(3, r_description);
	statement.setDate(4, date_applied);
	statement.setDate(5, date_released);
	statement.setDate(6, date_last_edited);
	statement.setInt(7, prep_time);
	statement.setBoolean(8, is_released);
	statement.setInt(9, difficulty_id);
	statement.setInt(10, cat_id);
	statement.setInt(11, cook_id);
	statement.executeUpdate();
	
	//Prepsteps
	final String insert_query_prepsteps = "INSERT INTO preparation values(?, ?, ?)";
	PreparedStatement statement6 = connection.prepareStatement(insert_query_prepsteps);
	statement6.setInt(1, prep_step);
	statement6.setInt(2, recp_id);
	statement6.setString(3, instruction);
	statement6.executeUpdate();
	
	//Uses
	final String insert_query_uses = "INSERT INTO uses values(?, ?, ?)";
	PreparedStatement statement5 = connection.prepareStatement(insert_query_uses);
	statement5.setInt(1, recp_id);
	statement5.setInt(2, unit_id);
	statement5.setInt(3, amount);
	statement5.executeUpdate();
	
	
	
	//Zutat zu der Collection hinzufügen
	ingredients.add(new IngredientHelper(ingr_id, ingr_name, amount, unit_id, unit_name));
	recipe.setIngredients(ingredients);
	recipe2.setIngredients(ingredients);
	
	//prepsteps
	prep_steps.add(new PreparationDTO(prep_step, instruction, recp_id));
	recipe.setPrepSteps(prep_steps);
	recipe2.setPrepSteps(prep_steps);
	
	
    }
    

    @After
    public void tearDown() throws Exception
    {
	
	//Uses
	final String delete_query_uses = "DELETE FROM uses WHERE recp_id=? AND ingr_id=?";
	PreparedStatement statement5 = connection.prepareStatement(delete_query_uses);
	statement5.setInt(1, recp_id);
	statement5.setInt(2, ingr_id);
	statement5.executeUpdate();
	
	//Prepsteps
	final String delete_query_prepsteps = "DELETE FROM preparation WHERE prep_step=? AND recp_id=?";
	PreparedStatement statement6 = connection.prepareStatement(delete_query_prepsteps);
	statement6.setInt(1, prep_step);
	statement6.setInt(2, recp_id);
	statement6.executeUpdate();
	
	//Rezept
	final String delete_query_recipe = "DELETE FROM Recipe WHERE recp_id=?";
	PreparedStatement statement = connection.prepareStatement(delete_query_recipe);
	statement.setInt(1, recp_id);
	statement.executeUpdate();
	
	//Rezept2
	final String delete_query_recipe2 = "DELETE FROM Recipe WHERE r_name=?";
	PreparedStatement statement8 = connection.prepareStatement(delete_query_recipe2);
	statement8.setString(1, r_name2);
	statement8.executeUpdate();
	
	//Kategorie
	final String delete_query_cat = "DELETE FROM category WHERE cat_id=?";
	PreparedStatement statement2 = connection.prepareStatement(delete_query_cat);
	statement2.setInt(1, cat_id);
	statement2.executeUpdate();
	
	//Zutat
	final String delete_query_ingr = "DELETE FROM ingredient WHERE ingr_id=?";
	PreparedStatement statement3 = connection.prepareStatement(delete_query_ingr);
	statement3.setInt(1, ingr_id);
	statement3.executeUpdate();
	
	//Einheit
	final String delete_query_unit = "DELETE FROM unit WHERE unit_id=?";
	PreparedStatement statement4 = connection.prepareStatement(delete_query_unit);
	statement4.setInt(1, unit_id);
	statement4.executeUpdate();
	
	
	//Koch
	final String delete_query_cook = "DELETE FROM Cook WHERE cook_id=?";
	PreparedStatement statement7 = connection.prepareStatement(delete_query_cook);
	statement7.setInt(1, cook_id);
	statement7.executeUpdate();
    }

    
    
    
    
    
    @Test
    public void testSearchByName()
    {
	String[] search_term = {r_name};
	LinkedList<RecipeHelper> recipes = (LinkedList<RecipeHelper>) RecipeDB.searchByName(connection, search_term);
	assertFalse("testSearchByName", recipes.isEmpty());
    }

    @Test
    public void testSearchByID()
    {
	LinkedList<RecipeHelper> recipes2 = (LinkedList<RecipeHelper>) RecipeDB.searchByID(connection, recp_id);
	assertFalse("testSearchByID", recipes2.isEmpty());
    }

    @Test
    public void testSearchByCookID()
    {
	LinkedList<RecipeHelper> recipes3 = (LinkedList<RecipeHelper>) RecipeDB.searchByCookID(connection, cook_id);
	assertFalse("testSearchByCookID", recipes3.isEmpty());
    }

    @Test
    public void testExistsName()
    {
	boolean exists = RecipeDB.existsName(connection, r_name);
	assertTrue("testExistsName", exists);
    }

    @Test
    public void testCreateNewRecipe()
    {
	boolean create = RecipeDB.createNewRecipe(connection, cook_id, r_name2, r_description3);
	assertTrue("testCreateNewRecipe", create);
    }

    @Test
    public void testOwnedBy()
    {
	int owned_by = RecipeDB.ownedBy(connection, recp_id);
	assertTrue("testOwnedBy", owned_by == cook_id);
    }

    @Test
    public void testIdByName()
    {
	int id = RecipeDB.IdByName(connection, r_name);
	assertTrue("testIdByName", id == recp_id);
    }

    @Test
    public void testAssignRecipesToChef()
    {
	int assigned = RecipeDB.assignRecipesToChef(connection, cook_id);
	assertTrue("testAssignRecipesToChef", assigned == 1);
    }

    @Test
    public void testGetByID()
    {
	LinkedList<RecipeHelper> recipes3 = (LinkedList<RecipeHelper>) RecipeDB.searchByID(connection, recp_id);
	assertFalse("testSearchByID", recipes3.isEmpty());
    }

}
