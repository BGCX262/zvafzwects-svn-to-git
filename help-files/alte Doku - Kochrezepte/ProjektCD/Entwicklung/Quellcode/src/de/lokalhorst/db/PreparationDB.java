package de.lokalhorst.db;

import java.sql.PreparedStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

//import com.sap.dbtech.jdbc.trace.Connection;

import de.lokalhorst.db.dto.*;

/**
 * 
 * @author Christoph Antes, Jochen Pätzold
 * @version 06.11.2010
 */
public class PreparationDB
{

    /**
     * Sucht alle Zubereitungsschritte zu einer bestimmen RezeptID und liefert
     * sie als Collection von PreparationDTOs zurück
     * 
     * @author Christoph Antes
     * @param cn
     *            Verbindung zur Datenbank
     * @param id
     *            RezeptID, nach der die zugehörigen Zubereitungsschritte
     *            gesucht werden
     * @return Collection von PreparationDTOs
     */
    public static Collection<PreparationDTO> getPrepStepsByRecpID(
	    final Connection cn, int recipe_id)
    {
	final LinkedList<PreparationDTO> result = new LinkedList<PreparationDTO>();

	String sql_query = "SELECT * FROM preparation WHERE recp_id = ?";

	try
	{
	    // Prepared Statement mit Platzhalter
	    PreparedStatement statement = cn.prepareStatement(sql_query);
	    statement.setString(1, "" + recipe_id);
	    ResultSet resultset = statement.executeQuery();

	    while (resultset.next())
	    {
		// neues Objekt mit den Daten aus dem Resultset erstellen
		PreparationDTO dto = new PreparationDTO(resultset);

		// erstelltes Objekt zu der LinkedList hinzufügen
		result.add(dto);
	    }

	    resultset.close();

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	// Die LinkedList mit allen "gefundenen" Daten (in Form von
	// PreparationDTO-Objekten) wird zurückgeliefert
	return result;
    }

    /**
     * Ändern der Zutatenliste zu einer RezeptID
     * 
     * @author Jochen Pätzold
     * @param cn2
     *            Dantebankverbindung OHNE autocommit
     * @param recp_id
     *            RezeptID
     * @param ingredients
     *            Liste der Zubereitungsschritt als RecipeHelper
     * @throws SQLException
     * @return Anzahl der Zutaten
     */
    public static int saveChanges(Connection cn2, int recp_id,
	    Collection<PreparationDTO> prep_steps) throws SQLException
    {
	{
	    final String delete_query = "DELETE FROM Preparation WHERE recp_id=?";
	    final String insert_query = "INSERT INTO Preparation VALUES(?, ?, ?)";

	    PreparedStatement ps = cn2.prepareStatement(delete_query);
	    ps.setInt(1, recp_id);
	    ps.executeUpdate();

	    int numInserted = 0;
	    ps = cn2.prepareStatement(insert_query);

	    Iterator<PreparationDTO> iter = prep_steps.iterator();
	    while (iter.hasNext())
	    {
		PreparationDTO step = iter.next();
		ps.setInt(1, step.getPrep_step()); // Zubereitungsschritt
		ps.setInt(2, recp_id); // RezeptID
		ps.setString(3, step.getInstruction()); // Beschreibung
		ps.executeUpdate();
		numInserted++;
	    }

	    return numInserted;
	}
    }
}
