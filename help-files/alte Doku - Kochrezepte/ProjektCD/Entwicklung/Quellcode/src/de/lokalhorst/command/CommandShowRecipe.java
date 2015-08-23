package de.lokalhorst.command;

import java.sql.Connection;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.RecipeDB;
import de.lokalhorst.helper.RecipeHelper;


/**
 * Diese Command-Klasse ist für die Anzeige von Rezepten zuständig
 * 
 * @author Christoph Antes, Christian Zöllner
 * @version 06.11.2010
 */

public class CommandShowRecipe extends Command
{
 
	@Override
    public CommandOutput execute(CommandInput input) throws Exception 
    {
		//Ergebnisobjekt anlegen
		CommandOutput output = new CommandOutput();
	
		//RezeptID, welche per Inputobjekt übergeben wurde
		String id = input.getParam("param");
		int recp_id = Integer.parseInt(id); 
			
		//Verbindung zu DB aufbauen
		Connection connect = ConnectDB.getPoolConnection();
	
	
		//Collection mit Rezept-Hilfsklasse
		RecipeHelper result = null;
	
		//Suchanfrage an die DB
		result = RecipeDB.getByID(connect, recp_id);
	
		//DB-Verbindung schließen
		ConnectDB.closeConnection(connect);
	
		
		//Result wird zu dem Outputobjekt hinzugefügt
		output.addValue("recipe", result);
	
		output.setTargetView("ShowRecipe");
	
		return output;
    }
}
