package de.lokalhorst.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedList;

import de.lokalhorst.db.dto.CategoryDTO;

/**
 * Klasse für den Zugriff auf die Category-Tabellen
 * 
 * @author Jochen Pätzold
 * @version 06.11.2010
 */
public class CategoryDB
{

    /**
     * Liefert einer Liste mit allen verfügbaren CategoryDTO
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * @return LinkedList von CategoryDTO
     */
    public static LinkedList<CategoryDTO> getCategories(Connection cn)
    {
	final String getAll_query = "SELECT * FROM Category";
	final LinkedList<CategoryDTO> result = new LinkedList<CategoryDTO>();

	try
	{
	    PreparedStatement ps = cn.prepareStatement(getAll_query);
	    ResultSet rs = ps.executeQuery();
	    while (rs.next())
	    {
		CategoryDTO dto = new CategoryDTO(rs);
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
     * Liefert den Namen zu einer CategoryID, null falls ID nicht existiert
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * @return Name der Kategorie
     */
    public static String NameById(Connection cn, int cat_id)
    {
	final String getName_query = "SELECT cat_name FROM Category WHERE cat_id=?";
	String result = null;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(getName_query);
	    ps.setInt(1, cat_id);
	    ResultSet rs = ps.executeQuery();
	    if (rs.next())
	    {
		result = rs.getString("cat_name");
	    }
	    rs.close();
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return result;
    }
}
