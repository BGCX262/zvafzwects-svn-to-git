package de.lokalhorst.command;

import java.sql.Connection;
import java.util.Iterator;
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
import de.lokalhorst.db.dto.PreparationDTO;
import de.lokalhorst.db.dto.UnitDTO;
import de.lokalhorst.helper.IngredientHelper;
import de.lokalhorst.helper.RecipeHelper;

/**
 * Bearbeiten eines Rezeptes
 * 
 * @author Jochen Pätzold
 * @version 05.11.10
 */
public class CommandEditRecipe extends Command
{
    @SuppressWarnings("unchecked")
    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();
	HttpSession session = input.getSession();
	RecipeHelper recipe = (RecipeHelper) session.getAttribute("recipe");
	LinkedList<IngredientHelper> ingredients = null;

	output.setInfoMsg(null);

	// Abweisen bei unangemeldetem Benutzer oder fehlender RezeptID
	if (session.getAttribute("cook") == null)
	{
	    output.setErrorMsg("Illegaler Seitenaufruf von \"Rezepte anlegen\" ohne Anmeldung!");
	    output.setTargetView("Default");
	    return output;
	}
	if (input.getParam("recp_id") == null)
	{
	    output.setErrorMsg("Illegaler Seitenaufruf von \"Rezepte anlegen\" ohne RezeptID!");
	    output.setTargetView("Default");
	    return output;
	}
	// Fehlerhafte RezeptID abweisen
	int recp_id = 0;
	try
	{
	    recp_id = Integer.parseInt(input.getParam("recp_id"));

	} catch (Exception e)
	{
	    output.setErrorMsg("Ungültige RezeptID angegeben!");
	    output.setTargetView("Default");
	    return output;
	}

	Connection cn = ConnectDB.getPoolConnection(true);

	int cookID = ((CookDTO) session.getAttribute("cook")).getCook_id();
	boolean isAdmin = ((CookDTO) session.getAttribute("cook")).isIs_admin();
	int ownerID = RecipeDB.ownedBy(cn, recp_id);

	// Unerlaubten Zugriff abweisen
	if (!(isAdmin || ownerID == cookID))
	{
	    output.setErrorMsg("Illegaler Seitenaufruf von \"Rezepte bearbeiten\". Sie dürfen diese Rezept nicht bearbeiten!");
	    output.setTargetView("Default");
	    ConnectDB.closeConnection(cn);
	    return output;
	}

	// Rezept aus DB laden, falls es nicht in der Session steht oder ein
	// altes ist
	if (recipe == null || recipe.getRecp_id() != recp_id)
	{
	    recipe = RecipeDB.getByID(cn, recp_id);

	    if (recipe == null)
	    { // Abbruch falls Rezept nicht gefunden wird
		output.setErrorMsg("Das Rezept mit der ID" + recp_id
			+ " konnte nicht gefunden werden!");
		output.setTargetView("Default");
		ConnectDB.closeConnection(cn);
		return output;
	    }

	    ingredients = IngredientDB.getIngredients(cn);
	    LinkedList<DifficultyDTO> difficulties = DifficultyDB
		    .getDifficulties(cn);
	    LinkedList<CategoryDTO> categories = CategoryDB.getCategories(cn);
	    LinkedList<UnitDTO> units = UnitDB.getUnits(cn);
	    // notwendige Daten in Session legen
	    session.setAttribute("recipe", recipe);
	    session.setAttribute("ingredients", ingredients);
	    session.setAttribute("difficulties", difficulties);
	    session.setAttribute("categories", categories);
	    session.setAttribute("units", units);
	} else
	{
	    ingredients = (LinkedList<IngredientHelper>) session
		    .getAttribute("ingredients");
	}

	LinkedList<PreparationDTO> prepsteps = (LinkedList<PreparationDTO>) recipe
		.getPrep_steps();

	// Edit-Cmd zerlegen
	String editCmd = input.getParam("edit");
	char ec;
	if (editCmd != null)
	{
	    ec = editCmd.toCharArray()[0];
	} else
	{
	    ec = ' ';
	}

	output.setTargetView("EditRecipe");
	int prepSize = prepsteps.size();
	String msg = "";

	// Verarbeitung abhängig von Parameter editCmd bzw. ec
	switch (ec)
	{
	case '1': // Zubereitungsschritt hinzufügen
	    msg = formToSession(input, recipe, prepsteps);
	    if (prepSize < 20) // Limit der Schritte
	    {
		msg += "Zubereitungsschritt " + (prepSize + 1)
			+ " angefügt! | ";
		prepsteps.addLast(new PreparationDTO(prepSize + 1, "", recipe
			.getRecp_id()));
	    } else
	    {
		msg += "Kein weiterer Zubereitungsschritt, das wird uns sonst zu kompliziert! | ";
	    }
	    break;
	case '2': // Zubereitungsschritt entfernen
	    msg = formToSession(input, recipe, prepsteps);
	    if (prepSize > 0)
	    {
		msg += "Zubereitungsschritt " + prepSize + " entfernt! | ";
		prepsteps.removeLast();
	    } else
	    {
		msg += "Kein weiterer Schritt mehr vorhanden! | ";
	    }
	    break;
	case '3': // Zutat entfernen
	    msg = formToSession(input, recipe, prepsteps);
	    if (input.getParam("ingr_id") != null)
	    {
		msg += removeIngredient(recipe, input.getParam("ingr_id"));
	    }
	    break;
	case '4': // Zutat hinzufügen
	    msg = formToSession(input, recipe, prepsteps);
	    String addAmount = input.getParam("addAmount");
	    String addIngr = input.getParam("addIngr");
	    msg += addIngredient(recipe, ingredients, addAmount, addIngr);
	    break;
	case '5': // neue Zutat anlegen
	    msg = formToSession(input, recipe, prepsteps);
	    String newIngr = input.getParam("newIngr");
	    // Abweisen wenn Zutat schon vorhanden
	    if (IngredientDB.existsName(cn, newIngr))
	    {
		msg = "Zutag \"" + newIngr + "\" existiert bereits! ";
		break;
	    }
	    // neue Zutat anlgen
	    if (newIngr != null && newIngr.length() > 0)
	    {
		int newUnit = Integer.parseInt(input.getParam("newUnit"));
		boolean success = false;
		success = IngredientDB.createIngredient(cn, newIngr, newUnit);
		if (success)
		{
		    ingredients = IngredientDB.getIngredients(cn);
		    session.setAttribute("ingredients", ingredients);
		}
	    }
	    break;
	case '6': // Speichern
	    msg = formToSession(input, recipe, prepsteps);
	    recipe.setIs_released(false);
	    if (input.getParam("release") != null)
	    {
		String validateMsg = recipe.validate();
		if (validateMsg.length() > 0)
		{ // Rezept kann nicht veröffentlicht werden
		    msg += validateMsg;
		    recipe.setIs_released(false);
		} else
		{ // Rezept wird veröffentlicht
		    msg = "Rezeptdaten geändert!";
		    recipe.setIs_released(true);
		}
	    }
	    Connection cn2 = ConnectDB.getPoolConnection(false);
	    RecipeDB.saveChanges(cn2, recipe);
	    ConnectDB.closeConnection(cn2);
	    break;
	default: // erster Aufruf
	    break;
	}
	output.setInfoMsg(msg);
	ConnectDB.closeConnection(cn); // DB Verbindung schließen

	return output;
    }

    /*
     * Fügt eine Zutat hinzu zum Rezept hinzu, vorhandene werden summiert
     */
    private String addIngredient(RecipeHelper recipe,
	    LinkedList<IngredientHelper> ingredients, String amount,
	    String ingr_id)
    {
	String msg = "";
	int newAmount = 0;
	try
	{
	    newAmount = Integer.parseInt(amount);
	} catch (Exception e)
	{
	    return msg;
	}
	int id = Integer.parseInt(ingr_id);

	// Ablaufen der Rezeptzutaten
	LinkedList<IngredientHelper> recp_ingr = (LinkedList<IngredientHelper>) recipe
		.getIngredients();
	Iterator<IngredientHelper> iter = recp_ingr.iterator();
	IngredientHelper ingr = null;
	while (iter.hasNext())
	{
	    ingr = (IngredientHelper) iter.next();
	    if (ingr.getIngr_id() == id)
	    { // Verrechnen der Zutat
		int oldAmount = ingr.getAmount();
		newAmount += oldAmount;
		if (newAmount > 0) // erlaubt das Reduzieren von Mengen
		    ingr.setAmount(newAmount);
		return "Menge von \"" + ingr.getIngr_name() + "\" auf "
			+ newAmount + " geändert!";
	    }
	}
	// Zutag hinzufügen
	int unit_id = 0;
	String ingr_name = null;
	String unit_name = null;
	iter = ingredients.iterator();
	ingr = null;
	while (iter.hasNext())
	{
	    ingr = iter.next();
	    if (ingr.getIngr_id() == id)
	    {
		unit_id = ingr.getUnit_id();
		ingr_name = ingr.getIngr_name();
		unit_name = ingr.getUnit_name();
		break;
	    }
	}
	IngredientHelper newIngredient = new IngredientHelper(id, ingr_name,
		newAmount, unit_id, unit_name);
	recp_ingr.addLast(newIngredient);
	return "" + newAmount + " " + ingr.getUnit_name() + " "
		+ ingr.getIngr_name() + " hinzugefügt!";
    }

    /*
     * Entferne Zutat zu gegebener ZutatenID aus der Liste
     */
    private String removeIngredient(RecipeHelper recipe, String ingr_id)
    {
	String msg = "";
	int id = Integer.parseInt(ingr_id);

	Iterator<IngredientHelper> iter = recipe.getIngredients().iterator();
	while (iter.hasNext())
	{
	    IngredientHelper ingr = (IngredientHelper) iter.next();
	    if (ingr.getIngr_id() == id)
	    {
		recipe.getIngredients().remove(ingr);
		msg = "Zutat \"" + ingr.getIngr_name() + "\" entfernt!";
		break;
	    }
	}
	return msg;
    }

    /*
     * Übertragen der Formularfelder in das Sessionobjekt Recipe. Textfelder
     * werden auf 300 Zeichen gekürzt
     */
    private String formToSession(CommandInput input, RecipeHelper recipe,
	    LinkedList<PreparationDTO> prepsteps)
    {
	String msg = "";
	input.getSession().setAttribute("release", input.getParam("release"));
	String recp = input.getParam("desc").trim();
	if (recp.length() > 300)
	{
	    recp = recp.substring(0, 300);
	    msg += "Beschreibung auf 300 Zeichen gekürzt | ";
	}
	recipe.setR_description(recp);
	int time = 0;
	try
	// Prüfen, weil Benutzereingabe
	{
	    time = Integer.parseInt(input.getParam("prep_time"));
	} catch (Exception e)
	{
	}
	recipe.setPrep_time(time);
	recipe.setDifficulty_id(Integer.parseInt(input.getParam("difficulty")));
	recipe.setCat_id(Integer.parseInt(input.getParam("category")));
	// Zubereitungsschritte ablaufen
	int count = 1;
	String param = null;
	PreparationDTO prepstep = null;
	while ((param = input.getParam("prepstep_" + count)) != null)
	{
	    param = param.trim();
	    if (param.length() > 300)
	    {
		param = param.substring(0, 300);
		msg += "Zubereitungsschritt " + count
			+ " auf 300 Zeichen gekürzt | ";
	    }
	    prepstep = prepsteps.get(count - 1);
	    prepstep.setPrep_step(count);
	    prepstep.setInstruction(param);
	    count++;
	}

	return msg;
    }
}
