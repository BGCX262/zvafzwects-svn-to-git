package de.lokalhorst.command;

import java.sql.Connection;
import java.util.LinkedList;

import javax.servlet.http.HttpSession;

import de.lokalhorst.db.CategoryDB;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.DifficultyDB;
import de.lokalhorst.db.IngredientDB;
import de.lokalhorst.db.RecipeDB;
import de.lokalhorst.db.UnitDB;
import de.lokalhorst.db.dto.CategoryDTO;
import de.lokalhorst.db.dto.CookDTO;
import de.lokalhorst.db.dto.DifficultyDTO;
import de.lokalhorst.db.dto.UnitDTO;
import de.lokalhorst.helper.IngredientHelper;

/**
 * Anlegen eines neuen Rezeptes
 * 
 * @author Jochen Pätzold
 * @version 04.11.10
 */
public class CommandCreateRecipe extends Command
{
    private final String success_msg = "Sie könne nun Ihr Rezept bearbeiten. Vergessen Sie nicht Ihr Rezept, wenn es vollständig ist, zu veröffentlichen.";
    private final String failed_msg = "Anlegen des neuen Rezeptes ist fehlgeschlagen, bitte versuchen Sie es erneut.";

    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();

	// Unangemeldete Nutzer abweisen
	if (input.getSession().getAttribute("cook") == null)
	{
	    output.setErrorMsg("Illegaler Seitenaufruf von \"Rezepte anlegen\" ohne Anmeldung!");
	    output.setTargetView("Default");
	    return output;
	}
	int cookID = ((CookDTO) input.getSession().getAttribute("cook"))
		.getCook_id();

	String name = input.getParam("name").trim();
	String desc = input.getParam("desc").trim();
	output.setInfoMsg(null);

	Connection cn = ConnectDB.getPoolConnection(true); // DB Verbindung
	String error_msg = checkForm(cn, name, desc);

	if (error_msg != null)
	{ // Eingabefehler aufgetreten: Meldung + Dialog bleibt stehen
	    output.addValue("name", name);
	    output.addValue("desc", desc);
	    output.setInfoMsg(error_msg);
	    output.setErrorMsg(null);
	    output.setTargetView("CreateRecipe");
	} else
	{ // Eingabe OK, neues Rezept wird in DB angelegt
	    boolean successful = RecipeDB.createNewRecipe(cn, cookID, name,
		    desc);
	    if (successful)
	    { // Erfolgreich angelegt
		output.setInfoMsg(success_msg);
		output.setErrorMsg(null);

		// Notwendige Daten in Session legen damit Rezept für die JSP
		// verfügbar ist
		int recp_id = RecipeDB.IdByName(cn, name);
		// Object recipe = RecipeDB.searchByID_single(cn, recp_id);
		Object recipe = RecipeDB.getByID(cn, recp_id);

		LinkedList<IngredientHelper> ingredients = IngredientDB
			.getIngredients(cn);
		LinkedList<DifficultyDTO> difficulties = DifficultyDB
			.getDifficulties(cn);
		LinkedList<CategoryDTO> categories = CategoryDB
			.getCategories(cn);
		LinkedList<UnitDTO> units = UnitDB.getUnits(cn);
		HttpSession session = input.getSession();
		session.setAttribute("recipe", recipe);
		session.setAttribute("ingredients", ingredients);
		session.setAttribute("difficulties", difficulties);
		session.setAttribute("categories", categories);
		session.setAttribute("units", units);
		//
		output.addValue("recp_id", recp_id);
		output.setTargetView("EditRecipe");
	    } else
	    { // Fehler beim Anlegen in der DB
		output.setInfoMsg(null);
		output.setErrorMsg(failed_msg);
		output.setTargetView("CreateRecipe");
	    }
	}

	ConnectDB.closeConnection(cn);

	return output;
    }

    /**
     * checkForm überprüft die Gültigkeit der Eingabefelder Name und
     * Beschreibung
     * 
     * @param cn
     *            Datenbankverbindung
     * @param name
     *            Name der registiert werden soll
     * @param desc
     *            Passwort das registiert werden soll
     */
    private String checkForm(Connection cn, String name, String desc)
    {
	String msg = null;
	// name
	if (name.length() < 5)
	    msg = "Der Name enthält weniger als 5 Zeichen!";
	else if (name.length() > 50)
	    msg = "Der Name enthält mehr als 50 Zeichen!";
	else if (RecipeDB.existsName(cn, name))
	    msg = "Den Rezept mit dem Namen \"" + name + "\" gibt es bereits!";
	// desc(cription)
	else if (desc.length() < 10)
	    msg = "Die Rezeptbeschreibung enthält weniger als 10 Zeichen!";
	else if (desc.length() > 300)
	    msg = "Die Rezeptbeschreibung ist länger als 300 Zeichen!";

	return msg;
    }

}
