package de.lokalhorst.command;

import java.sql.Connection;
import de.lokalhorst.helper.*;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.RecipeDB;
import de.lokalhorst.db.dto.CookDTO;
import java.util.Collection;
import java.util.Iterator;
import javax.servlet.http.HttpSession;


/**
 * Command zum Suchen nach den "eigenen" Rezepten, also den Rezepten, die der aktuell
 * eingeloggten cook_id entsprechen
 * 
 * @author Christoph Antes
 * @version 06.11.2010
 */
public class CommandSearchOwnRecipe extends Command
{
    @Override
    public CommandOutput execute( CommandInput input ) throws Exception 
    {	
	//Ergebnisobjekt anlegen
    	CommandOutput output = new CommandOutput();
    		
    	//Session holen
    	HttpSession session = input.getSession();
    		
    	//cook-objekt und cook_id des aktuell eingeloggten Users aus der Session holen
    	CookDTO cook = (CookDTO)session.getAttribute("cook");
	int cook_id = cook.getCook_id();
    		
    	//Verbindung zur DB aufbauen
    	Connection connect = ConnectDB.getPoolConnection();
				
    	//Collection für die Suchergebnisse
	Collection<RecipeHelper> results = null;

	//Suchanfrage an die DB
	results = RecipeDB.searchByCookID(connect, cook_id);
		
	//DB-Verbindung schließen
	ConnectDB.closeConnection(connect);
		
		
	//Anzahl suchergebnisse ermitteln
    	Iterator<RecipeHelper> iterator = results.iterator();
    		
    	int counter = 0;
    	while(iterator.hasNext())
	{
    	    counter++;
    	    iterator.next();
    	}
    		
    	//Infomessages für die Suchergebnisse setzen
    	switch(counter)
    	{
    		case 0: output.setInfoMsg("Sie haben noch keine eigenen Rezepte erstellt");break;
    		case 1: output.setInfoMsg("Sie haben bisher 1 Rezept erstellt");break;
    		default: output.setInfoMsg("Sie haben bisher "+counter+" Rezepte erstellt");break;
    	}
    		
    		
    	output.addValue("recipes", results);
    	output.setTargetView( "SearchresultOwnRecipe" );
    	return output;
    }
}

