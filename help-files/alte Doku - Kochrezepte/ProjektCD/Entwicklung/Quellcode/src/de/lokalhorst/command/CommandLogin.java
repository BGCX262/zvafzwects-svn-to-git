package de.lokalhorst.command;

import java.sql.Connection;

import de.lokalhorst.db.BuyListedDB;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.CookDB;
import de.lokalhorst.db.dto.CookDTO;
import de.lokalhorst.helper.BuyList;
import de.lokalhorst.helper.EncodePwd;

/**
 * Login am System
 * 
 * @author Jochen Pätzold
 * @version 25.10.10
 * 
 */
public class CommandLogin extends Command
{
    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();

	// Lesen der Formulardaten
	String name = input.getParam("username").trim();
	String password = input.getParam("password").trim();

	output.setTargetView("Default");

	if (name == "" || password == "") // simpler Test auf unvollst. Eingabe
	{
	    output.setInfoMsg("Name oder Passwort leer!"); // Hinweis setzen
	    output.setErrorMsg(null); // Fehlermeldung rücksetzen
	} else
	// Login überprüfen
	{
	    Connection cn = ConnectDB.getPoolConnection(true);
	    CookDTO cook = CookDB.loginCook(cn, name,
		    EncodePwd.encode(password));
	    ConnectDB.closeConnection(cn);
	    if (cook != null) // Login erfolgreich!
	    {
		output.setInfoMsg(cook.getCook_name()
			+ " hat die Küche betreten!"); // Hinweis
		output.setErrorMsg(null); // Fehlermeldung rücksetzen
		// CookDTO in Session setzen
		input.getSession().setAttribute("cook", cook);
		cn = ConnectDB.getPoolConnection();// liste aus db laden
		BuyList buylist = BuyListedDB
			.loadBuyList(cn, cook.getCook_id());
		ConnectDB.closeConnection(cn);
		// geladene liste in die session
		input.getSession().setAttribute("buylist", buylist);

	    } else
	    { // Login fehlgeschlagen
		output.setInfoMsg("Kombination von Name und Passwort falsch!"); // Hinweis
		output.setErrorMsg(null); // Fehlermeldung rücksetzen
	    }
	}

	return output;
    }

}
