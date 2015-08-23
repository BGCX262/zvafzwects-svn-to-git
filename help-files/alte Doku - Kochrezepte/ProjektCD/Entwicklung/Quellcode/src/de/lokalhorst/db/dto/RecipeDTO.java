package de.lokalhorst.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;

/**
 * RecipeDTO DataTransferObjekt zur Tabelle Recipe
 * 
 * @author Jacques Mwizerwa, Christoph Antes
 * @version 06.10.2011
 */
public class RecipeDTO
{
    private int recp_id;
    private String r_name;
    private String r_description;
    private Date date_applied;
    private Date date_last_edited;
    private int prep_time;
    private boolean is_released;
    private int categorieID;
    private int cookID;
    private int difficulityID;

    /**
     * Konstruktor mit Resultset als Parameter
     * 
     * @param resultset
     */
    public RecipeDTO(ResultSet resultset)
    {
	try
	{
	    recp_id = resultset.getInt("recp_id");
	    r_name = resultset.getString("r_name");
	    r_description = resultset.getString("r_description");
	    date_applied = resultset.getDate("date_applied");
	    date_last_edited = resultset.getDate("date_last_edited");
	    prep_time = resultset.getInt("prep_time");
	    is_released = resultset.getBoolean("is_released");
	    categorieID = resultset.getInt("cat_ID");
	    cookID = resultset.getInt("cook_ID");
	    difficulityID = resultset.getInt("diff_level");

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    // Getter- und Setter-Methoden

    public int getRecp_id()
    {
	return recp_id;
    }

    public void setRecp_id(int recp_id)
    {
	this.recp_id = recp_id;
    }

    public String getR_name()
    {
	return r_name;
    }

    public void setR_name(String r_name)
    {
	this.r_name = r_name;
    }

    public String getR_description()
    {
	return r_description;
    }

    public void setR_description(String r_description)
    {
	this.r_description = r_description;
    }

    public Date getDate_applied()
    {
	return date_applied;
    }

    public void setDate_applied(Date date_applied)
    {
	this.date_applied = date_applied;
    }

    public Date getDate_last_edited()
    {
	return date_last_edited;
    }

    public void setDate_last_edited(Date date_last_edited)
    {
	this.date_last_edited = date_last_edited;
    }

    public int getPrep_time()
    {
	return prep_time;
    }

    public void setPrep_time(int prep_time)
    {
	this.prep_time = prep_time;
    }

    public boolean isIs_released()
    {
	return is_released;
    }

    public void setIs_released(boolean is_released)
    {
	this.is_released = is_released;
    }

    public int getCategorieID()
    {
	return categorieID;
    }

    public void setCategorieID(int categorieID)
    {
	this.categorieID = categorieID;
    }

    public int getCookID()
    {
	return cookID;
    }

    public void setCookID(int cookID)
    {
	this.cookID = cookID;
    }

    public int getDifficulityID()
    {
	return difficulityID;
    }

    public void setDifficulityID(int difficulityID)
    {
	this.difficulityID = difficulityID;
    }

    @Override
    public String toString()
    {
	return "RecipeDTO [recp_id=" + recp_id + ", r_name=" + r_name
		+ ", r_description=" + r_description + ", date_applied="
		+ date_applied + ", date_last_edited=" + date_last_edited
		+ ", prep_time=" + prep_time + ", is_released=" + is_released
		+ ", categorieID=" + categorieID + ", cookID=" + cookID
		+ ", difficulityID=" + difficulityID + "]";
    }

}
