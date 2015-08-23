package de.lokalhorst.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

import de.lokalhorst.helper.IngredientHelper;

/**
 * Klasse für den Zugriff auf die Uses-Tabelle
 * 
 * @author Jochen Pätzold
 * @version 10.11.2010
 */
public class UsesDB
{

    /**
     * Ändern der Zutatenliste
     * 
     * @author Jochen Pätzold
     * @param cn2
     *            Dantebankverbindung OHNE autocommit
     * @param recp_id
     *            RezeptID
     * @param ingredients
     *            Liste der Zutaten als RecipeHelper
     * @throws SQLException
     * @return Anzahl der Zutaten
     */
    public static int saveChanges(Connection cn2, int recp_id,
	    Collection<IngredientHelper> ingredients) throws SQLException
    {
	final String delete_query = "DELETE FROM Uses WHERE recp_id=?";
	final String insert_query = "INSERT INTO Uses VALUES(?, ?, ?)";

	PreparedStatement ps = cn2.prepareStatement(delete_query);
	ps.setInt(1, recp_id);
	ps.executeUpdate();

	int numInserted = 0;
	ps = cn2.prepareStatement(insert_query);

	Iterator<IngredientHelper> iter = ingredients.iterator();
	while (iter.hasNext())
	{
	    IngredientHelper ingr = iter.next();
	    ps.setInt(1, recp_id); // RezeptID
	    ps.setInt(2, ingr.getIngr_id()); // ZutatenID
	    ps.setInt(3, ingr.getAmount()); // Menge
	    ps.executeUpdate();
	    numInserted++;
	}

	return numInserted;
    }
}
