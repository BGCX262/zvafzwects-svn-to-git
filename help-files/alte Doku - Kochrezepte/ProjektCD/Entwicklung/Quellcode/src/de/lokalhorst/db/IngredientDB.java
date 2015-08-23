package de.lokalhorst.db;

import de.lokalhorst.helper.*;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.LinkedList;

/**
 * 
 * @author Christoph Antes, Jochen Pätzold
 * @version 06.11.2010
 */

public class IngredientDB
{

    /**
     * Sucht alle Zutaten, die zu einer bestimmten Rezept-ID gehören
     * 
     * @author Christoph Antes
     * @param cn
     *            Datenbankvebrindung
     * @param id
     *            Rezept-ID, zu der die zugehörigen Zutaten gesucht werden
     *            sollen
     * @return Collection von IngredientHelpern
     */
    public static Collection<IngredientHelper> getIngredientsByRecipeID(
	    final Connection cn, final int id)
    {
	final LinkedList<IngredientHelper> result = new LinkedList<IngredientHelper>();

	String sql_query = "Select uses.ingr_id, ingr_name, amount, unit_id, unit_name FROM ingredient JOIN uses ON (ingredient.ingr_id = uses.ingr_id) JOIN unit ON (unit.unit_id = ingredient.ingr_unit) WHERE recp_id = ?";

	try
	{
	    // Prepared Statement mit Platzhalter
	    PreparedStatement statement = cn.prepareStatement(sql_query);
	    statement.setString(1, "" + id);

	    // Ausführen der Query
	    ResultSet resultset = statement.executeQuery();

	    while (resultset.next())
	    {
		// Neues Objekt mit den Daten aus/ dem Resultset erstellen
		IngredientHelper dto = new IngredientHelper(resultset);

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
     * Liefert einer Liste mit allen verfügbaren Zutaten als IngredientHelper,
     * die Menge wird dabei auf 0 gesetzt
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * @return LinkedList IngredientHelper
     */
    public static LinkedList<IngredientHelper> getIngredients(
	    final Connection cn)
    {
	final String getAll_query = "SELECT ingr_id, ingr_name, unit_id, unit_name "
		+ "FROM Ingredient JOIN Unit ON (unit_id = ingr_unit) "
		+ "ORDER BY ingr_name ASC";
	final LinkedList<IngredientHelper> result = new LinkedList<IngredientHelper>();

	try
	{
	    PreparedStatement ps = cn.prepareStatement(getAll_query);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next())
	    {
		int ingr_id = rs.getInt("ingr_id");
		String ingr_name = rs.getString("ingr_name");
		int unit_id = rs.getInt("unit_id");
		String unit_name = rs.getString("unit_name");

		IngredientHelper dto = new IngredientHelper(ingr_id, ingr_name,
			0, unit_id, unit_name);
		result.add(dto);
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return result;
    }

    /**
     * Anlegen einer neuen Zutat
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * @param newIngr
     *            Name der Zutat
     * @param newUnit
     *            Id der zugehörigen Einheit
     * @return true wenn erfolgreich, false sonst
     */
    public static boolean createIngredient(final Connection cn, String newIngr,
	    int newUnit)
    {
	final String create_query = "INSERT INTO Ingredient VALUES(ingr_id_seq.nextval, ?, ?)";
	int numInserted = 0;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(create_query);
	    ps.setString(1, newIngr);
	    ps.setInt(2, newUnit);

	    numInserted = ps.executeUpdate(); // Einfügen in DB

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return (numInserted != 0);
    }

    /**
     * Anlegen einer neuen Zutat
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * @param newIngr
     * @return true wenn erfolgreich, false sonst
     */
    public static boolean existsName(Connection cn, String newIngr)
    {
	final String exists_query = "SELECT * FROM Ingredient WHERE LOWER(ingr_name)=?";
	boolean result = false;
	try
	{
	    PreparedStatement ps = cn.prepareStatement(exists_query);
	    ps.setString(1, newIngr.trim().toLowerCase());
	    ResultSet rs = ps.executeQuery();
	    if (rs.next())
	    {
		result = true;
	    }

	} catch (Exception e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
	return result;
    }
}
