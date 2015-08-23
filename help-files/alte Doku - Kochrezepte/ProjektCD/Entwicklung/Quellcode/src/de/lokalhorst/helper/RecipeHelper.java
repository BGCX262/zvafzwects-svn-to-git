package de.lokalhorst.helper;

import java.sql.ResultSet;

import java.sql.SQLException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;
import java.util.Date;
import java.util.LinkedList;

import com.sun.org.apache.xerces.internal.impl.xpath.regex.ParseException;

import de.lokalhorst.db.dto.*;

/**
 * Hilfsklasse um ein "komplettes" Rezept darzustellen (für die Suche, Anzeige
 * und Erstellung)
 * 
 * @author Christoph Antes, Christian Zöllner
 * @version 0.1
 */

public class RecipeHelper
{
    private int recp_id;
    private String r_name;
    private String r_description;
    private Date date_applied;
    private Date date_last_edited;
    private int prep_time;
    private boolean is_released;
    private int cat_id;
    private int cook_id;
    private int difficulty_id;
    private String difficulty;
    private int persons;

    private String cook_name;
    private boolean is_admin;

    private String cat_name;

    private Collection<IngredientHelper> ingredients;
    private Collection<PreparationDTO> prep_steps;

    public RecipeHelper()
    {
	ingredients = new LinkedList<IngredientHelper>();
	prep_steps = new LinkedList<PreparationDTO>();

    }

    /**
     * Konstruktor mit Resultset als Übergabeparameter
     * 
     * @param resultset
     */
    public RecipeHelper(ResultSet resultset)
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
	    cat_id = resultset.getInt("cat_ID");
	    cook_id = resultset.getInt("cook_ID");
	    difficulty_id = resultset.getInt("diff_level");
	    difficulty = resultset.getString("description");
	    cook_name = resultset.getString("cook_name");
	    is_admin = resultset.getBoolean("is_admin");
	    cat_name = resultset.getString("cat_name");

	    persons = 4; // Standardpersonenzahl

	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    /**
     * Konstruktor mit 3 Stringarrays als Parameter
     * 
     * @param recipeattributes
     * @param ingredients
     * @param prepsteps
     * @throws java.text.ParseException
     */
    public RecipeHelper(String[] recipeattributes, String[] ing, String[] prep)
	    throws java.text.ParseException
    {
	recp_id = Integer.parseInt(recipeattributes[0]);
	r_name = recipeattributes[1];
	r_description = recipeattributes[2];
	persons = Integer.parseInt(recipeattributes[3]);
	prep_time = Integer.parseInt(recipeattributes[4]);
	difficulty = recipeattributes[5];
	difficulty_id = Integer.parseInt(recipeattributes[6]);
	cat_name = recipeattributes[7];
	cat_id = Integer.parseInt(recipeattributes[8]);

	try
	{
	    SimpleDateFormat format;
	    format = new SimpleDateFormat("dd.MM.yy");
	    date_applied = format.parse(recipeattributes[9]);
	    date_last_edited = format.parse(recipeattributes[10]);
	} catch (ParseException e)
	{
	    System.err.println("ParseException :" + e);
	}

	cook_name = recipeattributes[11];
	cook_id = Integer.parseInt(recipeattributes[12]);
	is_admin = Boolean.parseBoolean(recipeattributes[13]);
	is_released = Boolean.parseBoolean(recipeattributes[14]);

	// Zutaten hinzufügen
	// Da jede Zutat aus 3 Attributen besteht, gibt es length/3 Zutaten

	// Neue LinkedList für die Zutaten
	ingredients = new LinkedList<IngredientHelper>();

	for (int counter = 0; counter < ing.length - 1; counter = counter + 3)
	{
	    IngredientHelper tmp = new IngredientHelper(ing[counter],
		    ing[counter + 1], ing[counter + 2]);
	    this.addIngredient(tmp);
	}

	// Zubereitungsschritte hinzufügen

	// neue LinkedList für die Zubereitungsschritte
	prep_steps = new LinkedList<PreparationDTO>();

	// counter = 1, da [0] Null ist aufgrund des "_" am anfang des Strings
	for (int counter = 1; counter < prep.length; counter++)
	{
	    PreparationDTO tmp = new PreparationDTO(prep[counter], this.recp_id);
	    this.addPrepStep(tmp);
	}

    }
    
    
    
    
    /**
     * Konstruktor mit allen Attributen als Übergabeparameter
     */
    public RecipeHelper(int recp_id, String r_name, String r_description, Date date_applied, Date date_last_edited, 
	    int prep_time, boolean is_released, int cat_id, int cook_id, int difficulty_id, String difficulty, String cook_name,
	    boolean is_admin, String cat_name, int persons)
    {
	this.recp_id = recp_id;
	this.r_name = r_name;
	this.r_description = r_description;
	this.date_applied = date_applied;
	this.date_last_edited = date_last_edited;
	this.prep_time = prep_time;
	this.is_released = is_released;
	this.cat_id = cat_id;
	this.cook_id = cook_id;
	this.difficulty_id = difficulty_id;
	this.difficulty = difficulty;
	this.cook_name = cook_name;
	this.is_admin = is_admin;
	this.cat_name = cat_name;
	this.persons = persons;
	
	this.ingredients = new LinkedList<IngredientHelper>();
	this.prep_steps = new LinkedList<PreparationDTO>();
	
    }

    //TEST
    public RecipeHelper(RecipeDTO dto)
    {
	this.cat_id = dto.getCategorieID();
	this.cat_name = null;
	this.cook_id = dto.getCookID();
	this.cook_name = null;
	this.date_applied = dto.getDate_applied();
	this.date_last_edited = dto.getDate_last_edited();
	this.difficulty = null;
	this.difficulty_id = dto.getDifficulityID();
	this.ingredients = null;
	this.is_admin = false;
	this.is_released = dto.isIs_released();
	this.persons = 4;
	this.prep_steps = null;
	this.prep_time = dto.getPrep_time();
	this.r_description = dto.getR_description();
	this.r_name = dto.getR_name();
	this.recp_id = dto.getRecp_id();
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

    public String getDate_applied()
    {
	DateFormat df = DateFormat.getDateInstance();
	String date = df.format(this.date_applied);

	return date;
    }

    public Date getDate_applied_date()
    {
	return this.date_applied;
    }

    public void setDate_applied(Date date_applied)
    {
	this.date_applied = date_applied;
    }

    public String getDate_last_edited()
    {
	DateFormat df = DateFormat.getDateInstance();
	String date = df.format(this.date_last_edited);

	return date;
    }

    public Date getDate_last_edited_date()
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

    public int getDifficulty_id()
    {
	return difficulty_id;
    }

    public void setDifficulty_id(int difficulityID)
    {
	this.difficulty_id = difficulityID;
    }

    public void setDifficulty(String diff)
    {
	this.difficulty = diff;
    }

    public String getDifficulty()
    {
	return difficulty;
    }

    public int getCook_id()
    {
	return cook_id;
    }

    public void setCook_id(int cook_id)
    {
	this.cook_id = cook_id;
    }

    public String getCook_name()
    {
	return cook_name;
    }

    public void setCook_name(String cook_name)
    {
	this.cook_name = cook_name;
    }

    public boolean isIs_admin()
    {
	return is_admin;
    }

    public void setIs_admin(boolean is_admin)
    {
	this.is_admin = is_admin;
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

    public void setIngredients(Collection<IngredientHelper> ingr)
    {
	this.ingredients = ingr;
    }

    public Collection<IngredientHelper> getIngredients()
    {
	return ingredients;
    }

    public void addIngredient(IngredientHelper ingr)
    {
	this.ingredients.add(ingr);
    }

    public void setPrepSteps(Collection<PreparationDTO> prep)
    {
	this.prep_steps = prep;
    }

    public Collection<PreparationDTO> getPrep_steps()
    {
	return prep_steps;
    }

    public void addPrepStep(PreparationDTO prep_step)
    {
	this.prep_steps.add(prep_step);
    }

    public int getPersons()
    {
	return persons;
    }

    public void setPersons(int persons)
    {
	this.persons = persons;
    }

    public String toString()
    {
	return "RecipeDTO [recp_id=" + recp_id + "\nr_name=" + r_name
		+ "\n r_description=" + r_description + "\n date_applied="
		+ date_applied + "\n date_last_edited=" + date_last_edited
		+ "\n prep_time=" + prep_time + "\n is_released=" + is_released
		+ "\n categorieID=" + cat_id + "\n cookID=" + cook_id
		+ "\n cook_name= " + cook_name + "\ndifficulty_id="
		+ difficulty_id + "\n difficulty= " + difficulty
		+ "\n ingredients= " + ingredients + "\n prep_steps= "
		+ prep_steps + "]";
    }

    /**
     * Validate überprüft ob genügend Angaben vorliegen um ein Rezept zu
     * veröffentlichen. Wenn das Rezept gülitg ist wird Leerstring ""
     * zurückgegeben, sonst ein String mit den Fehlern
     * 
     * @return Fehlerstring
     */
    public String validate()
    {
	String valMsg = "";
	if (r_description.length() < 10)
	    valMsg += "Beschreibung enthält weniger als 10 Zeichen | ";
	if (prep_time < 1)
	    valMsg += "Zubereitungszeit kürzer als 1 Minute | ";
	// Zutaten
	if (ingredients.isEmpty())
	    valMsg += "Keine Zutatvorhanden | ";
	// Schritte
	if (prep_steps.isEmpty())
	    valMsg += "Kein Zubereitungsschritt angegeben | ";
	return valMsg;
    }

}
