package de.lokalhorst.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.lokalhorst.db.dto.UnitDTO;

/**
 * Klasse für den Zugriff auf die Unit-Tabelle
 * 
 * @author Jochen Pätzold
 * @version 09.11.10
 */
public class UnitDB
{

    /**
     * Liste aller Einheiten
     * 
     * @author Jochen Pätzold
     * @param cn
     * @return LinkedList von UnitDTO
     */
    public static LinkedList<UnitDTO> getUnits(Connection cn)
    {
	final String getAll_query = "SELECT unit_id, unit_name FROM Unit";
	final LinkedList<UnitDTO> result = new LinkedList<UnitDTO>();

	try
	{
	    PreparedStatement ps = cn.prepareStatement(getAll_query);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next())
	    {
		int id = rs.getInt("unit_id");
		String name = rs.getString("unit_name");
		UnitDTO dto = new UnitDTO(id, name);

		result.add(dto);
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return result;
    }

}
