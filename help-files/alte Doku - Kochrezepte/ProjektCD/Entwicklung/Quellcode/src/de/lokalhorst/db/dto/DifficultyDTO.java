package de.lokalhorst.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DifficultyDTO DataTransferObjekt zur Tabelle Difficulty
 * 
 * @author Jacques Mwizerwa, Jochen Pätzold
 * @version 10.10.2010
 */
public class DifficultyDTO
{
    private int diff_level;
    private String description;

    /**
     * DifficultyDTO Konstruktor mit Resultset als Parameter
     * 
     * @author Jochen Pätzold
     * @param rs
     *            Resultset
     */
    public DifficultyDTO(ResultSet rs)
    {
	try
	{
	    diff_level = rs.getInt("diff_level");
	    description = rs.getString("description");
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    public int getDiff_level()
    {
	return diff_level;
    }

    public void setDiff_level(int diff_level)
    {
	this.diff_level = diff_level;
    }

    public String getDescription()
    {
	return description;
    }

    public void setDescription(String description)
    {
	this.description = description;
    }

    @Override
    public String toString()
    {
	return "DifficultyDTO [diff_level=" + diff_level + ", description="
		+ description + "]";
    }

}
