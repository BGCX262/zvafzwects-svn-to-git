package de.lokalhorst.command;

import java.sql.Connection;

import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.CookDB;
import de.lokalhorst.db.dto.CookDTO;

/**
 * Geheime Frage um das Passwort neu zu setzen
 * 
 * @author Jochen Pätzold
 * @version 28.10.10
 * 
 */
public class CommandSecretName extends Command
{
    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();

	// Lesen der Formulardaten
	String name = input.getParam("name").trim();

	if ((name == null) || (name.length() == 0))
	{ // kein Name angegeben
	    output.setInfoMsg("Bitte geben Sie einen Namen an!");
	    output.setTargetView("SecretName"); // Nachfolgeseite
	    output.addValue("name", name); // Namensfeld durchreichen

	} else
	{ // Name angegeben
	    Connection cn = ConnectDB.getPoolConnection(true); // DB Verbindung
	    CookDTO cook = CookDB.findByName(cn, name);
	    ConnectDB.closeConnection(cn); // DB Verbindung schließen
	    if (cook == null)
	    { // Name nicht registriert
		output.setInfoMsg("Es ist kein Koch mit dem Namen \"" + name
			+ "\" registriert!");
		output.setTargetView("SecretName"); // Nachfolgeseite
		output.addValue("name", null); // Namensfeld löschen
	    } else
	    { // Name gefunden, weiter zur Geheimfrage
	      // Suchergenis in Session setzen
		input.getSession().setAttribute("sec_cook", cook);
		output.setTargetView("SecretQuestion"); // Nachfolgeseite
	    }
	}

	return output;
    }

}
