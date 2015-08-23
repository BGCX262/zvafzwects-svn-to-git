package de.lokalhorst.db.dto;

/**
 * UsesDTO DataTransferObjekt zur Tabelle Uses
 * 
 * @author Jacques Mwizerwa
 * @version 19.10.2010
 */
public class UsesDTO
{

    private int recipeID;
    private int ingredientID;
    private int amount;

    public int getRecipeID()
    {
	return recipeID;
    }

    public void setRecipeID(int recipeID)
    {
	this.recipeID = recipeID;
    }

    public int getIngredientID()
    {
	return ingredientID;
    }

    public void setIngredientID(int ingredientID)
    {
	this.ingredientID = ingredientID;
    }

    public int getAmount()
    {
	return amount;
    }

    public void setAmount(int amount)
    {
	this.amount = amount;
    }

    @Override
    public String toString()
    {
	return "UsesDTO [recipeID=" + recipeID + ", ingredientID="
		+ ingredientID + ", amount=" + amount + "]";
    }

}
