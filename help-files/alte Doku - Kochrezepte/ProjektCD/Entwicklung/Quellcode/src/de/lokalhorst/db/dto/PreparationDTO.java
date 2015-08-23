package de.lokalhorst.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * PreparationDTO DataTransferObjekt zur Tabelle Preparation
 * 
 * @author Jacques Mwizerwa, Christoph Antes
 * @version 19.10.2010
 */
public class PreparationDTO
{
    private int prep_step;
    private String instruction;
    private int recipeID; // ID des Rezepts

    /**
     * Vollständiger Konstruktor
     * 
     * @param prep_step
     * @param instruction
     * @param recipeID
     */
    public PreparationDTO(int prep_step, String instruction, int recipeID)
    {
	super();
	this.prep_step = prep_step;
	this.instruction = instruction;
	this.recipeID = recipeID;
    }

    // Konstruktor mit Resultset als Übergabeparamter
    public PreparationDTO(ResultSet rset)
    {
	try
	{
	    prep_step = rset.getInt("prep_step");
	    recipeID = rset.getInt("recp_id");
	    instruction = rset.getString("instruction");

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    // Konstruktor mit String und Int als Übergabeparamter
    public PreparationDTO(String instr, int recp_id)
    {
	this.prep_step = 0;
	this.recipeID = recp_id;
	this.instruction = instr;
    }

    public int getPrep_step()
    {
	return prep_step;
    }

    public void setPrep_step(int prep_step)
    {
	this.prep_step = prep_step;
    }

    public String getInstruction()
    {
	return instruction;
    }

    public void setInstruction(String instruction)
    {
	this.instruction = instruction;
    }

    public int getRecipeID()
    {
	return recipeID;
    }

    public void setRecipeID(int recipeID)
    {
	this.recipeID = recipeID;
    }

    public String toString()
    {
	return instruction + "\n";
    }

}
