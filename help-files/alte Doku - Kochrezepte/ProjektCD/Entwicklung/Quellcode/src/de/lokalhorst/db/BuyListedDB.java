package de.lokalhorst.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;

import de.lokalhorst.helper.BuyList;
import de.lokalhorst.helper.BuyListEntry;
import de.lokalhorst.helper.IngredientHelper;

/**
 * 
 * @author Jochen Pätzold, Christian Zöllner
 * @version 08.11.10
 */

public class BuyListedDB
{
    private static final String clear_query = "DELETE Buylisted WHERE cook_id=?";
    private static final String save_query = "INSERT Buylisted VALUES (?, ?, ?)";
    private static final String load_query = "select BUYLISTED.recp_id,r_name,quantity,amount,USES.INGR_ID, INGREDIENT.INGR_NAME, UNIT.UNIT_NAME from BUYLISTED "
	    + "inner join RECIPE on BUYLISTED.RECP_ID = RECIPE.RECP_ID "
	    + "inner join USES on BUYLISTED.RECP_ID = USES.RECP_ID "
	    + "inner join INGREDIENT on USES.INGR_ID = INGREDIENT.INGR_ID "
	    + "inner join UNIT on UNIT.UNIT_ID = INGREDIENT.INGR_UNIT "
	    + "where BUYLISTED.cook_id =? " + "order by BUYLISTED.recp_id";

    /**
     * Löschen der Einkaufsliste für einen Koch
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * 
     * @param cookID
     *            ID des Kochs dessen Einkaufsliste gelöscht werden soll
     * 
     * @return Anzahl der gelöschten Einträge
     */
    public static int clearBuylist(final Connection cn, int cookID)
    {
	int numDeleted = 0;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(clear_query);
	    ps.setInt(1, cookID);
	    numDeleted = ps.executeUpdate(); // DB abfragen
	} catch (Exception e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return numDeleted;

    }

    /**
     * Speichern der Einkaufsliste in Datenbank
     * 
     * @author Jochen Pätzold
     * @param cn
     *            Datenbankverbindung
     * 
     * @param cookID
     *            ID unter der die Liste gespeichert werden soll
     * 
     * @return Anzahl der eingefügten Rezepte
     */
    public static int saveBuyList(final Connection cn, int cookID,
	    Collection<BuyListEntry> buyList)
    {
	int numInserted = 0;

	try
	{
	    PreparedStatement ps = cn.prepareStatement(save_query);
	    ps.setInt(1, cookID);

	    Iterator<BuyListEntry> it = buyList.iterator();

	    while (it.hasNext())
	    {
		BuyListEntry entry = it.next();
		ps.setInt(2, entry.getRecp_id());
		ps.setInt(3, entry.getQuantity());
		ps.executeUpdate();
		numInserted++;

	    }
	} catch (Exception e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return numInserted;
    }

    /**
     * Laden der Einkaufsliste aus der Datenbank
     * 
     * @author Christian Zöllner
     * @param cn
     *            Datenbankverbindung
     * 
     * @param cookID
     *            ID für die die Liste geladen werden soll
     * 
     * @return BuyList
     */
    public static BuyList loadBuyList(final Connection cn, int cookID)
    {
	LinkedList<IngredientHelper> ingredients = new LinkedList<IngredientHelper>();
	BuyList buylist = new BuyList();

	try
	{
	    PreparedStatement ps = cn.prepareStatement(load_query);
	    ps.setInt(1, cookID);
	    ResultSet rs = ps.executeQuery();

	    // Cursor auf erste Spalte setzen, falls Einkaufsliste leer ist
	    // staus false
	    boolean status = rs.next();

	    // Aufbau der Einkaufsliste
	    while (status)
	    {
		int recp_id = rs.getInt("recp_id");
		String r_name = rs.getString("r_name");
		int quantity = rs.getInt("quantity");

		// Solange Zutaten zum selben Rezept gehören ?
		while (rs.getInt("recp_id") == recp_id)
		{
		    // Berechnung Zutatenmenge für geg Personenzahl
		    double ratio = ((double) quantity / 4);
		    double help_amount = Math.ceil(rs.getInt("amount") * ratio);
		    int amount = (int) (help_amount);

		    ingredients
			    .add(new IngredientHelper(amount, rs
				    .getString("unit_name"), rs
				    .getString("ingr_name")));

		    status = rs.next();

		    if (!status)
		    {
			// break nötig, weil sonst noch einmal die
			// Whilebedingug geprüft wird => Exception
			break;
		    }

		}

		// Einkauflisteneintrag anlegen
		BuyListEntry entry = new BuyListEntry(r_name, recp_id,
			quantity, ingredients);
		buylist.add(entry);

		// Neue Ingredientlite wegen Call by Reference
		ingredients = new LinkedList<IngredientHelper>();
	    }

	    rs.close();
	} catch (Exception e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}

	return buylist;

    }
}
