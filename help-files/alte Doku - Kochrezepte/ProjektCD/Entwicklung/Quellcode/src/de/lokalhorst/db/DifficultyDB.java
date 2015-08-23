package de.lokalhorst.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.lokalhorst.db.dto.DifficultyDTO;

/**
 * Klasse für den Zugriff auf die Difficulty-Tabellen
 * 
 * @author Jochen Pätzold
 * @version 06.11.2010
 */
public class DifficultyDB
{

    /**
     * Liefert einer Liste mit allen verfügbaren DifficultyDTO
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * @return LinkedList DifficultyDTO
     */
    public static LinkedList<DifficultyDTO> getDifficulties(Connection cn)
    {
	final String getAll_query = "SELECT * FROM Difficulty";
	final LinkedList<DifficultyDTO> result = new LinkedList<DifficultyDTO>();

	try
	{
	    PreparedStatement ps = cn.prepareStatement(getAll_query);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next())
	    {
		DifficultyDTO dto = new DifficultyDTO(rs);
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
     * Liefert die Beschreibung eines Schwierigkeitsgrades zu einer ID
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * @return Beschreibung des Schwierigkeitsgrades
     */
    public static String getDescriptionByLevel(Connection cn, int diff_level)
    {
	final String getDesc_query = "SELECT description FROM Difficulty WHERE diff_level=?";
	String result = null;
	try
	{
	    PreparedStatement ps = cn.prepareStatement(getDesc_query);
	    ps.setInt(1, diff_level);
	    ResultSet rs = ps.executeQuery();
	    if (rs.next())
	    {
		result = rs.getString("description");
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return result;
    }

}
