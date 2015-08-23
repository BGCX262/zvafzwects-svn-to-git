package de.lokalhorst.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * BuyListedDTO DataTransferObjekt zur Tabelle Buylisted
 * 
 * @author Jochen Pätzold
 * @version 30.10.2010
 */

public class BuyListedDTO
{
    private int cookID;
    private int recipeID;
    private int quantity; // Anzahl der Personen

    /**
     * CookDTO Konstruktor mit Resultset als Parameter
     * 
     * @param rs
     *            Resultset
     */
    public BuyListedDTO(ResultSet rs)
    {
	try
	{
	    cookID = rs.getInt("cook_id");
	    recipeID = rs.getInt("recipeID");
	    quantity = rs.getInt("quantity");

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    @Override
    public String toString()
    {
	return "BuyListedDTO [cookID=" + cookID + ", recipeID=" + recipeID
		+ ", quantity=" + quantity + "]";
    }

    public int getCookID()
    {
	return cookID;
    }

    public void setCookID(int cookID)
    {
	this.cookID = cookID;
    }

    public int getRecipeID()
    {
	return recipeID;
    }

    public void setRecipeID(int recipeID)
    {
	this.recipeID = recipeID;
    }

    public int getQuantity()
    {
	return quantity;
    }

    public void setQuantity(int quantity)
    {
	this.quantity = quantity;
    }

}
