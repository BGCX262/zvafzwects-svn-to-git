package de.lokalhorst.command;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.CookDB;
import de.lokalhorst.db.RecipeDB;
import de.lokalhorst.db.dto.CookDTO;

/**
 * Klasse löscht einen bestimmten User und überträgt seine Rezepte auf den
 * Superadmin
 * 
 * @author Christoph Antes
 * @version 12.11.2010
 * 
 */
public class CommandDeleteCook extends Command
{

    private CommandOutput executeDeletion(int cook_id, String cook_name)
    {
	CommandOutput out = new CommandOutput();
	int updated_recipes = 0; // Anzahl auf den Superadmin übertragener
				 // Rezepte

	// Verbindung zur DB aufbauen
	Connection connect = ConnectDB.getPoolConnection(true);

	// Rezepte des Users auf den Chefkoch übertragen
	updated_recipes = RecipeDB.assignRecipesToChef(connect, cook_id);

	if (updated_recipes != -1) // wenn die Übrtragung der Rezepte auf den
				   // Superadmin erfolgreich war
	{

	    // User aus der Datenbank löschen
	    boolean success = CookDB.deleteCook(connect, cook_id);

	    if (success)
	    {
		out.setInfoMsg("Änderungen erfolgreich übernommen! Der User \""
			+ cook_name + "\" wurde gelöscht.");

		// Setzen der Anzahl der geänderten Rezepte in der Infomessage
		if (updated_recipes == 1)
		{
		    out.setInfoMsg(out.getInfoMsg()
			    + " || 1 Rezept wurde auf den Chefkoch übertragen.");
		} else
		{
		    out.setInfoMsg(out.getInfoMsg() + " || " + updated_recipes
			    + " Rezepte wurden auf den Chefkoch übertragen.");
		}

		out.setTargetView("AdminFunctions");
	    } else
	    {
		out.setErrorMsg("Löschvorgang nicht erfolgreich. Der User \""
			+ cook_name
			+ "\" wurde nicht gelöscht. Bitte probieren Sie es erneut.");
		out.setTargetView("AdminFunctions");
	    }
	} else
	// Übertragung der Rezepte auf den Superadmin war nicht erfolgreich.
	// Löschvorgang wird nicht durchgeführt
	{
	    out.setErrorMsg("Es ist ein Fehler aufgetreten. Der User \""
		    + cook_name
		    + "\" wurde nicht gelöscht. Bitte probieren Sie es erneut.");
	}

	// DB-Verbindung schließen
	ConnectDB.closeConnection(connect);

	return out;
    }

    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	// Ergebnisobjekt anlegen
	CommandOutput output = new CommandOutput();

	int cook_id = Integer.parseInt(input.getParam("cook_id"));
	String cook_name = input.getParam("cook_name");

	// Eingeloggter User über die Session holen
	HttpSession session = input.getSession();
	CookDTO cook = (CookDTO) session.getAttribute("cook");

	if (cook.isIs_admin()) // Ist der User admin?
	{
	    // Wenn es sich bei dem zu löschenden User nicht um den eigenen-
	    // oder Chef-Account handelt.
	    // der Superdadmin und der eigene Account können nicht gelöscht
	    // werden
	    if (cook_id != 1 && cook_id != cook.getCook_id())
	    {
		output = executeDeletion(cook_id, cook_name);
	    }
	    // Schutz vor Zugriff über Parameter der URL
	    else if (cook_id == 1) // Man versucht den Chefaccount zu löschen
				   // (Nur direkt über die URL möglich, da kein
				   // Löschbutton vorhanden)
	    {
		output.setInfoMsg("Fehlgeschlagen! Keine Berechtigung den Superadmin zu löschen.");
		output.setTargetView("AdminFunctions");
	    } else
	    // Man versucht den eigenen Account zu löschen (Auch nur über URL
	    // möglich)
	    {
		output.setInfoMsg("Fehlgeschlagen! Sie dürfen ihren eigenen Account nicht löschen.");
		output.setTargetView("AdminFunctions");
	    }
	} else
	// der User ist kein Admin
	{
	    output.setInfoMsg("Keine Berechtigung! Sie sind nicht als Administrator eingeloggt.");
	    output.setTargetView("Default");
	}

	return output;
    }// execute()
}
