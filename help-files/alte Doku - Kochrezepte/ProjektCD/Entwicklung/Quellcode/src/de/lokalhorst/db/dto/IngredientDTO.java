package de.lokalhorst.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * IndredientDTO DataTransferObjekt zur Tabelle Ingredient
 * 
 * @author Jacques Mwizerwa, Jochen Pätzold
 * @version 19.10.2010
 */

public class IngredientDTO
{
    private int ingr_id;
    private String ingr_name;
    private int unit; // ID des Units

    /**
     * IngredientDTO Konstruktor mit Resultset als Parameter
     * 
     * @author Jochen Pätzold
     * @param rs
     *            Resultset
     */
    public IngredientDTO(ResultSet rs)
    {
	try
	{
	    ingr_id = rs.getInt("ingr_id");
	    ingr_name = rs.getString("ingr_name");
	    unit = rs.getInt("ingr_unit");
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    public int getIngr_id()
    {
	return ingr_id;
    }

    public void setIngr_id(int ingr_id)
    {
	this.ingr_id = ingr_id;
    }

    public String getIngr_name()
    {
	return ingr_name;
    }

    public void setIngr_name(String ingr_name)
    {
	this.ingr_name = ingr_name;
    }

    public int getUnit()
    {
	return unit;
    }

    public void setUnit(int unit)
    {
	this.unit = unit;
    }

    @Override
    public String toString()
    {
	return "IngredientDTO [ingr_id=" + ingr_id + ", ingr_name=" + ingr_name
		+ ", unit=" + unit + "]";
    }

}
