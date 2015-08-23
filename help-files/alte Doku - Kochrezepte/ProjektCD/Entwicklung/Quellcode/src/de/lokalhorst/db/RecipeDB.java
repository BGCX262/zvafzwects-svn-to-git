package de.lokalhorst.db;

import de.lokalhorst.db.dto.CookDTO;
import de.lokalhorst.db.dto.RecipeDTO;
import de.lokalhorst.helper.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.sql.Date;
import java.util.LinkedList;

/**
 * 
 * @author Christoph Antes, Christian Zöllner, Jochen Pätzold
 * @version 0.1
 */

public class RecipeDB
{

    /**
     * Liefert eine Liste von RecipeHelper, zu einem oder mehreren Suchmustern.
     * Die Suche arbeitet dabei NICHT case-sensitive
     * 
     * @author Christoph Antes
     * @param cn
     *            DB-Verbindung
     * @param search_term_array
     *            Liste von Suchmuster
     * @return LinkedList von RecipeHelpern
     */
    public static Collection<RecipeHelper> searchByName(final Connection cn,
	    final String[] search_term_array)
    {
	final LinkedList<RecipeHelper> result = new LinkedList<RecipeHelper>();

	String sql_query = "SELECT * FROM recipe JOIN cook ON (recipe.cook_id = cook.cook_id) JOIN category ON (recipe.cat_id = category.cat_id) JOIN difficulty ON (recipe.diff_level=difficulty.diff_level) "
		+ "WHERE recipe.is_released=true AND ("
		+ "lower(r_name) LIKE ?";

	// Je nach Anzahl der Elemente im übergebenen Array wird an den
	// Query-String ein weiteres Suchkriterium angefügt
	for (int counter = 1; counter < search_term_array.length; counter++)
	{
	    sql_query = sql_query + " OR lower(r_name) LIKE ?";
	}
	sql_query = sql_query + ")";

	try
	{
	    PreparedStatement statement = cn.prepareStatement(sql_query);

	    // Prepared Statement: Alle ? durch die Suchbegriffe des Arrays
	    // ersetzen
	    for (int counter = 0; counter < search_term_array.length; counter++)
	    {
		statement.setString(counter + 1, "%"
			+ search_term_array[counter].toLowerCase() + "%");
	    }

	    ResultSet resultset = statement.executeQuery();

	    while (resultset.next())
	    {
		// Neues Objekt mit den Daten aus dem Resultset erstellen
		RecipeHelper dto = new RecipeHelper(resultset);

		// erstelltes Objekt zu der LinkedList hinzufügen
		result.add(dto);
	    }

	    resultset.close();

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	// Die LinkedList mit allen "gefundenen" Daten wird zurückgeliefert
	return result;
    }

    /**
     * Holt ein bestimmtes Rezept mit der angegebenen ID aus der Datenbank und
     * setzt alle notwendigen Attribute, die für die Ausgabe des Rezeptes
     * notwendig sind. Um die Zutaten zu setzen wird auf die Funktion
     * "searchByRecipeID" der Klasse "IngredientDB" zugegriffen.
     * 
     * @author Christoph Antes
     * @param cn
     *            Verbindung zur Datenbank
     * @param recipe_id
     *            RezeptID, nach der gesucht werden soll
     * @return Collection von RecipeHelpern
     */
    public static Collection<RecipeHelper> searchByID(final Connection cn,
	    final int recipe_id)
    {
	final LinkedList<RecipeHelper> result = new LinkedList<RecipeHelper>();

	String sql_query = "SELECT * FROM recipe JOIN cook ON (recipe.cook_id = cook.cook_id) JOIN category ON (recipe.cat_id = category.cat_id) JOIN difficulty ON (recipe.diff_level=difficulty.diff_level) WHERE recipe.recp_id=?";

	try
	{
	    PreparedStatement statement = cn.prepareStatement(sql_query);
	    statement.setString(1, "" + recipe_id);
	    ResultSet resultset = statement.executeQuery();
	    while (resultset.next())
	    {
		// Neues Objekt mit den Daten aus dem Resultset erstellen
		RecipeHelper dto = new RecipeHelper(resultset);
		dto.setPrepSteps(PreparationDB.getPrepStepsByRecpID(cn,
			recipe_id));
		dto.setIngredients(IngredientDB.getIngredientsByRecipeID(cn,
			recipe_id));

		// erstelltes Objekt zu der LinkedList hinzufügen
		result.add(dto);
	    }
	    resultset.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	// Die LinkedList mit allen "gefundenen" Daten wird zurückgeliefert
	return result;
    }

    /**
     * Methode gibt alle Rezepte zurück, die zu einer bestimmten Cook-ID
     * gehören
     * 
     * @author Christoph Antes
     * @param cn
     *            Verbindung zur Datenbank
     * @param cook_id
     *            Cook-ID, nach der gesucht werden soll
     * @return
     */
    public static Collection<RecipeHelper> searchByCookID(final Connection cn,
	    final int cook_id)
    {
	final LinkedList<RecipeHelper> result = new LinkedList<RecipeHelper>();

	String sql_query = "SELECT * FROM recipe JOIN cook ON (recipe.cook_id = cook.cook_id) JOIN category ON (recipe.cat_id = category.cat_id) JOIN difficulty ON (recipe.diff_level=difficulty.diff_level) WHERE recipe.cook_id=?";

	try
	{
	    PreparedStatement statement = cn.prepareStatement(sql_query);
	    statement.setString(1, "" + cook_id);
	    ResultSet resultset = statement.executeQuery();

	    while (resultset.next())
	    {
		// Neues Objekt mit den Daten aus dem Resultset erstellen

		RecipeHelper dto = new RecipeHelper(resultset);
		result.add(dto); // erstelltes Objekt zu der LinkedList
				 // hinzufügen
	    }
	    resultset.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return result; // Die LinkedList mit allen "gefundenen" Daten wird
		       // zurückgeliefert
    }

    /**
     * Prüft ob ein Rezept(name) bereits existiert, wobei der Namensvergleich
     * NICHT case-sensitive ist.
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param r_name
     *            Name des zu suchende Rezeptes
     * @return boolean
     */
    public static boolean existsName(final Connection cn, String r_name)
    {
	final String exist_query = "SELECT r_name FROM Recipe WHERE LOWER(r_name)=?";
	boolean exists = false;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(exist_query);
	    ps.setString(1, r_name.toLowerCase());
	    ResultSet rs = ps.executeQuery();
	    if (rs.next()) // Prüfe auf Ergebnismennge
	    {
		exists = true;
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return exists;
    }

    /**
     * Anlegen eines neuen Rezepts mit Name und Beschreibung. Datumsfelder
     * werden auf aktuelles Datum gesetzt, alle anderen werden auf * @author
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Verbindung zur Datenbank
     * @param cookID
     *            ID des Rezepteschreibers
     * @param name
     *            Name des neuen Rezepts
     * @param desc
     *            Beschreibung des Rezeptes
     * @return Stausmeldung ob das Anlegen erfolgreich war
     */
    public static boolean createNewRecipe(final Connection cn, int cookID,
	    String name, String desc)
    {
	final String create_query = "INSERT INTO Recipe values(recp_id_seq.nextval, ?, ?, ?, ?, ?, 0, false, 1, 1, ?)";

	int numInserted = 0;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(create_query);
	    ps.setString(1, name); // Rezeptname
	    ps.setString(2, desc); // Beschreibung
	    Date now = new Date(new java.util.Date().getTime());
	    ps.setDate(3, now); // applied
	    ps.setDate(4, now); // released
	    ps.setDate(5, now); // last edited
	    ps.setInt(6, cookID); // ID des Rezepteschreibers

	    numInserted = ps.executeUpdate(); // Einfügen in DB

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return (numInserted != 0);
    }

    /**
     * Liefert die CookID zu einem Rezept, oder -1 im Falle eines DB-Fehlers
     * 
     * @author Jochen Pätzold
     * 
     * @param cn
     *            Verbindung zur Datenbank
     * @param recipe_id
     *            ID des zu suchenden Rezepts
     * @return cookID ID des Kochs zum gesuchten Rezept
     */
    public static int ownedBy(final Connection cn, int recipe_id)
    {
	final String owned_query = "SELECT cook_id FROM Recipe WHERE recp_id=?";
	int cookID = -1;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(owned_query);
	    ps.setInt(1, recipe_id);
	    ResultSet rs = ps.executeQuery(); // DB abfragen
	    if (rs.next()) // Prüfe auf Ergebnismennge
	    {
		cookID = rs.getInt("cook_id");
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return cookID;
    }

    /**
     * Liefert die RecipeID zu einem Rezeptnamen, 0 wenn nich gefunden oder -1
     * im Falle eines DB-Fehlers
     * 
     * @author Jochen Pätzold
     * 
     * @param cn
     *            Verbindung zur Datenbank
     * @param name
     *            Name des zu suchenden Rezepts
     * @return Rezept ID zu gegebenem Rezeptnamen
     */
    public static int IdByName(Connection cn, String name)
    {
	final String id_query = "SELECT recp_id FROM Recipe WHERE r_name=?";
	int result = 0;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(id_query);
	    ps.setString(1, name);
	    ResultSet rs = ps.executeQuery();
	    if (rs.next())
	    {
		result = rs.getInt("recp_id");
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	    result = -1;
	}

	return result;
    }

    /**
     * Speichern der Änderungen des Rezeptes
     * 
     * @author Jochen Pätzold
     * 
     * @param cn2
     *            Datenbankverbindung OHNE autocommit
     * @param recipe
     *            Rezept des Änderungen gespeichert werden
     */
    public static boolean saveChanges(Connection cn2, RecipeHelper recipe)
    {
	final String update_query = "UPDATE Recipe "
		+ " SET r_description=?, date_released=?, date_last_edited=?, "
		+ " prep_time=? ,is_released=?, diff_level=? ,cat_id=?"
		+ " WHERE recp_id=?";

	int numUpdated = 0;

	try
	{
	    // Rezeptdaten ändern
	    Date now = new Date(new java.util.Date().getTime());
	    PreparedStatement ps = cn2.prepareStatement(update_query);
	    ps.setString(1, recipe.getR_description());
	    ps.setDate(2, now); // released
	    ps.setDate(3, now); // last edited
	    ps.setInt(4, recipe.getPrep_time());
	    ps.setBoolean(5, recipe.isIs_released());
	    ps.setInt(6, recipe.getDifficulty_id());
	    ps.setInt(7, recipe.getCat_id());
	    ps.setInt(8, recipe.getRecp_id());
	    numUpdated = ps.executeUpdate();

	    // Zutatenliste ändern
	    UsesDB.saveChanges(cn2, recipe.getRecp_id(),
		    recipe.getIngredients());

	    // Zubereitungsschritte ändern
	    PreparationDB.saveChanges(cn2, recipe.getRecp_id(),
		    recipe.getPrep_steps());

	    cn2.commit();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	    try
	    {
		cn2.rollback();
	    } catch (Exception e2)
	    {
		System.err.println("Ein DB-Fehler ist aufgetreten:"
			+ e.toString());
	    }
	    numUpdated = 0;
	}

	return (numUpdated != 0);
    }

    /**
     * Überträgt alle Rezepte eines bestimmten Users auf den Superadmin (Chef)
     * 
     * @author Christoph Antes
     * @param cn
     *            Connection zur DB
     * @param cook_id
     *            ID des Users, dessen Rezepte auf den Superadmin übertragen
     *            werden sollen
     * @return Anzahl der geänderten Datensätze. -1 Bei DB-Fehler
     */
    public static int assignRecipesToChef(final Connection cn, final int cook_id)
    {

	String sql_query = "UPDATE recipe SET cook_id = 1 WHERE cook_id = ?";

	int numUpdates = 0; // Anzahl durchgeführter Updates

	try
	{
	    PreparedStatement statement = cn.prepareStatement(sql_query);

	    // Prepared Statement: ? durch "cook_id" ersetzen
	    statement.setInt(1, cook_id);

	    numUpdates = statement.executeUpdate();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());

	    // Rückgabewert -1, falls ein Fehler aufgetreten ist
	    numUpdates = -1;
	}

	return numUpdates;
    }

    /**
     * Liefert zu einer RezeptID eine RecipeHelper mit allen rezeptrelevanten
     * Informationene oder null wenn ID nicht vorhanden oder eine Fehler beim
     * Zusammensetzendes Rezepts auftrat
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbaknverbindung
     * @param recp_id
     *            ID ds Rezepts
     * @return RecipeHelper mit allen Zubereitungsschritten und Zutaten
     */
    public static RecipeHelper getByID(Connection cn, int recp_id)
    {
	final String get_query = "SELECT * FROM Recipe WHERE recp_id=?";
	RecipeHelper result = null;
	try
	{
	    PreparedStatement ps = cn.prepareStatement(get_query);
	    ps.setInt(1, recp_id);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next())
	    {
		RecipeDTO dto = new RecipeDTO(rs);
		RecipeHelper rh = new RecipeHelper(dto);

		rh.setCat_name(CategoryDB.NameById(cn, rh.getCat_id()));

		CookDTO cook = CookDB.findByID(cn, rh.getCook_id());
		rh.setCook_name(cook.getCook_name());
		rh.setIs_admin(cook.isIs_admin());

		rh.setDifficulty(DifficultyDB.getDescriptionByLevel(cn,
			rh.getDifficulty_id()));
		rh.setPrepSteps(PreparationDB.getPrepStepsByRecpID(cn, recp_id));
		rh.setIngredients(IngredientDB.getIngredientsByRecipeID(cn,
			recp_id));

		result = rh;
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
	return result;
    }

}
