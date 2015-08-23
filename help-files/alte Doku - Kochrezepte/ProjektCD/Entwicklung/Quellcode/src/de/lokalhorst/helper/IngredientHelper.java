package de.lokalhorst.helper;

import java.sql.ResultSet;

import java.sql.SQLException;

/**
 * Hilfsklasse um Zutaten für ein Rezept mit zugehörigen Einheiten anzeigen zu können
 * 
 * @author Christoph Antes, Christian Zöllner
 * @version 01.11.2010
 */

public class IngredientHelper
{
    private  int ingr_id;
	private String ingr_name;
	private int amount;
	private int unit_id; 
	private String unit_name;
	
	
	/**
	 * Konstruktor mit Resultset als Parameter
	 * @param resultset
	 */
	public IngredientHelper(ResultSet resultset) 
	{
	    try 
	    {
			ingr_id = resultset.getInt("ingr_id");
			ingr_name = resultset.getString("ingr_name");
			amount = resultset.getInt("amount");
			unit_id = resultset.getInt("unit_id");
			unit_name = resultset.getString("unit_name");
	    
	    }	catch (SQLException e) 
	 	{
			System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	 	}
	}
		
	
	/**
	 * Vollsändiger Konstruktor
	 * 
	 * @param ingr_id
	 * @param ingr_name
	 * @param amount
	 * @param unit_id
	 * @param unit_name
	 */
	public IngredientHelper(int ingr_id, String ingr_name, int amount,
		int unit_id, String unit_name)
	{
	    super();
	    this.ingr_id = ingr_id;
	    this.ingr_name = ingr_name;
	    this.amount = amount;
	    this.unit_id = unit_id;
	    this.unit_name = unit_name;
	}



	/**
	 * Konstruktor mit allen Attributen als Parameter
	 * @param ingr_name
	 * 				Name der Zutat
	 * @param amount
	 * 				Menge der Zutat
	 * @param unit_name
	 * 				Name der Menge
	 */
	public IngredientHelper( int amount, String unit_name, String ingr_name)
	{
	    this.ingr_name = ingr_name;
	    this.amount = amount;
	    this.unit_name = unit_name;
	}

		
	
	/**
	 * Konstruktor mit Strings als Parameter
	 * @param amount
	 * 				Menge der Zutat
	 * @param unit
	 * 				Name der Menge
	 * @param name
	 * 				Name der Zutat
	 */
	public IngredientHelper(String amount, String unit_name, String ingr_name)
	{
	    this.amount = Integer.parseInt(amount);
	    this.unit_name = unit_name;
	    this.ingr_name = ingr_name;
	}
	
	
	//Getter- und Setter-Methoden
	    
	public int getIngr_id()
	{
	    return ingr_id;
	}
	
	public void setIngr_id(int ingr_id)
	{
	    this.ingr_id = ingr_id;
	}
	    
	public void setIngr_name(String ingr_name)
	{
	    this.ingr_name = ingr_name;
	}
	
	public String getIngr_name()
	{
	    return ingr_name;
	}
	
	public int getUnit_id()
	{
	    return unit_id;
	}
	
	public void setUnit_id(int unit_id)
	{
	    this.unit_id = unit_id;
	}
	
	public void setUnit_name(String unit_name)
	{
	    this.unit_name = unit_name;
	}
	
	public String getUnit_name()
	{
	    return unit_name;
	}
	
	public int getAmount()
	{
	    return amount;
	}
	
	public void setAmount(int amount)
	{
	    this.amount = amount;
	}
	    
	
}
