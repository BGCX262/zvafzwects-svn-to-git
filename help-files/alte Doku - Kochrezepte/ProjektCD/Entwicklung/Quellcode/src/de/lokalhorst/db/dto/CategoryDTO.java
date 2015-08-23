package de.lokalhorst.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CategorieDTO DataTransferObjekt zur Tabelle Categorie
 * 
 * @author Jacques Mwizerwa
 * @version 19.10.2010
 */
public class CategoryDTO
{
    private int cat_id;
    private String cat_name;

    /**
     * CategoryDTO Konstruktor mit Resultset als Parameter
     * 
     * @author Jochen Pätzold
     * @param rs
     *            Resultset
     */
    public CategoryDTO(ResultSet rs)
    {
	try
	{
	    cat_id = rs.getInt("cat_id");
	    cat_name = rs.getString("cat_name");
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    public int getCat_id()
    {
	return cat_id;
    }

    public void setCat_id(int cat_id)
    {
	this.cat_id = cat_id;
    }

    public String getCat_name()
    {
	return cat_name;
    }

    public void setCat_name(String cat_name)
    {
	this.cat_name = cat_name;
    }

    @Override
    public String toString()
    {
	return "CategorieDTO [cat_id=" + cat_id + ", cat_name=" + cat_name
		+ "]";
    }

}
