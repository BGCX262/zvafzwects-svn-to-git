package de.lokalhorst.command;

import java.sql.Connection;
import de.lokalhorst.helper.*;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.RecipeDB;
import java.util.Collection;
import java.util.Iterator;



/**
 * Command sucht nach Rezepten anhand des eingegeben Namens (Suchbegriff)
 * 
 * @author Christoph Antes, Christian Zöllner
 * @version 12.11.2010
 */
public class CommandSearchRecipe extends Command
{
    
    /**
     * 
     * @param input
     * 		Command Input-Objekt
     * @param search_term_array
     * 		Suchterm-Array
     * @return
     */
    private CommandOutput executeSearch(CommandInput input, String[] search_term_array)
    {
	
	CommandOutput out = new CommandOutput();
	
	Collection<RecipeHelper> results = null;
	
	
	
	//Anzahl der "leeren Elemente" ""  im Array bestimmen
	int counter_not_space = 0;
	
	for(int counter=0; counter < search_term_array.length; counter++)
	{
	    if(!(search_term_array[counter].equals("")))
	    {
		counter_not_space++;
	    }
	}
	
	
	if(counter_not_space==0) //Sofern keine erlaubten Zeichen in einem Arrayelement stehen
	{
	    out.setInfoMsg("Bitte geben Sie einen sinnvollen Suchbegriff ein");
	}
	else
	{
	    //Für die Suche prepariertes Array erstellen
	    String[] help_array = SearchTermPreparer.prepareForSearch(search_term_array, counter_not_space);
	    
	    
	    if(SearchTermPreparer.validTermLength(help_array)) //Suche nur ausführen, wenn kein gesuchtes Wort weniger als 3 Zeichen enthält
	    {
		//Verbindung zur DB aufbauen
		Connection connect = ConnectDB.getPoolConnection();
	        	      
		//Suchanfrage an die DB
		results = RecipeDB.searchByName(connect, help_array);
	        	    
		//DB-Verbindung schließen
		ConnectDB.closeConnection(connect);  
	        	  
		    
		//Anzahl der Suchergebnisse ermitteln
		Iterator<RecipeHelper> iterator = results.iterator();
	        	    		
		 int counter = 0;
		 while(iterator.hasNext())
		 {
		     counter++;
		     iterator.next();
		 }
	        	    		
		 switch(counter)
		 {
		 	case 0: out.setInfoMsg("Ihre Suche nach \""+input.getParam("search_term")+"\" lieferte leider keine Ergebnisse");break;
	        	case 1: out.setInfoMsg("Ihre Suche nach \""+input.getParam("search_term")+"\" lieferte 1 Ergebnis");break;
	        	case 99: out.setInfoMsg("Ihre Suche nach \""+input.getParam("search_term")+"\" lieferte vermutlich mehr als 100 Ergebnisse. Sie sollten Ihre Suche genauer spezifizieren. Es werden die ersten 100 Treffer angezeigt.");break;
	        	default: out.setInfoMsg("Ihre Suche nach \""+input.getParam("search_term")+"\" lieferte "+counter+" Ergebnisse");break;
		 }
		
	    }
	    else //mindestens ein Suchbegriff ist kürzer als 3 Zeichen
	    {
		out.setInfoMsg("Mindestens einer Ihrer Suchbegriffe enthält weniger als 3 Zeichen. Bitte spezifizieren Sie Ihre Suche etwas genauer.");
	    }
	}
	
	out.addValue("recipes", results);
	  
	return out;
    }
    
    
    
    
    
    
    
    
    
    
	
	
	
	
    
    @Override
    public CommandOutput execute( CommandInput input ) throws Exception 
    {
	//Ergebnisobjekt anlegen
    	CommandOutput output = new CommandOutput();
    		
    	Collection<RecipeHelper> results = null;

	//Suchbegriff aus dem Input-Objekt holen
	String search_term = input.getParam("search_term");

	//entfernen der "nicht erlaubten" Sonderzeichen. Erlaubt sind "A-Z", "a-z", "0-9", "-" und "_"
	//Die Sonderzeichen werden durch "_" ersetzt
	search_term = search_term.replaceAll("[_[^\\wäüöÄÜÖ\\-]]", "_"); 
  
	//Suchterm aufsplitten. Trennzeichen ist "_"
	String[] search_term_array = search_term.split("_");
	
	if(search_term.isEmpty())
	{
	    output.setInfoMsg("Bitte geben Sie einen Suchbegriff ein");
	    
	    //Resultate werden zu dem Outputobjekt hinzugefügt: results = NULL
	    output.addValue("recipes", results);
	}
	else //wenn Suchbegriff nicht leer, dann Suche ausführen
	{
	    output = executeSearch(input, search_term_array); 
	}
	
    	
    	//Target-JSP wird gesetzt
    	output.setTargetView( "SearchresultByName" );

    	return output;

    }

}

