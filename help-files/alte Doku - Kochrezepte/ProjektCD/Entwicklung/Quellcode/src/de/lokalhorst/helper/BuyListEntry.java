package de.lokalhorst.helper;

import java.util.Collection;
import java.util.LinkedList;

/**
 * Hilfsklasse zum Verwalten der Einträge der Einkaufsliste
 * 
 * @author Christian Zöllner
 * @version 01.11.2010
 */
public class BuyListEntry
{
    private String r_name;
    private int recp_id;
    private int quantity;
    private String last_changed;
    
    private LinkedList<IngredientHelper> ingredients;

    // Konstruktoren

    public BuyListEntry()
    {
	this.r_name = null;
	this.recp_id = 0;
	this.quantity = 0;
	this.last_changed = null;
	this.ingredients = null;
    }

    public BuyListEntry(String r_name,String last_changed, LinkedList<IngredientHelper> ingredients)
    {
	this.r_name = r_name;
	this.last_changed = last_changed;
	this.ingredients = ingredients;
    }
   
    public BuyListEntry(String r_name,int recp_id, int quantity, LinkedList<IngredientHelper> ingredients)
    {
	this.r_name = r_name;
	this.recp_id = recp_id;
	this.quantity = quantity;
	this.last_changed = null;
	this.ingredients = ingredients;
    }
    
    

    public BuyListEntry(String r_name, String recp_id, String quantity,
	    LinkedList<IngredientHelper> ingredients)
    {
	this(r_name, Integer.parseInt(recp_id), Integer.parseInt(quantity), ingredients);
    }

    // Equals
    public boolean equals(Object obj)
    {
	if (obj instanceof BuyListEntry)// ist objekt auch buylistentry
	{
	    BuyListEntry bl = (BuyListEntry) obj; // cast

	    if (this.r_name.equals(bl.r_name)) // vergleich der namen
	    {
		return true;
	    } else
	    {
		return false;
	    }
	}

	return super.equals(obj); // wenn obj kein buylistentry
    }

    // Getter- und Setter

    public String getR_name()
    {
	return r_name;
    }

    public void setR_name(String r_name)
    {
	this.r_name = r_name;
    }

    /**
     * @return the last_changed
     */
    public String getLast_changed()
    {
        return last_changed;
    }

    /**
     * @param last_changed the last_changed to set
     */
    public void setLast_changed(String last_changed)
    {
        this.last_changed = last_changed;
    }

    public Collection<IngredientHelper> getIngredients()
    {
	return ingredients;
    }

    public void setIngredients(LinkedList<IngredientHelper> ingredients)
    {
	this.ingredients = ingredients;
    }

    public int getRecp_id()
    {
        return recp_id;
    }

    public void setRecp_id(int recp_id)
    {
        this.recp_id = recp_id;
    }

    public int getQuantity()
    {
        return quantity;
    }

    public void setQuantity(int quantity)
    {
        this.quantity = quantity;
    }

    /**
     * Methode fügt Zutat hinzu, null wird nicht hinzugefügt
     * 
     * @param ingredient
     *            hinzuzufügende Zutat
     */
    public void AddIngredients(IngredientHelper in)
    {
	if (ingredients == null)
	{
	    ingredients = new LinkedList<IngredientHelper>();
	}

	if (in != null)
	{
	    ingredients.add(in);
	}
    }

}
