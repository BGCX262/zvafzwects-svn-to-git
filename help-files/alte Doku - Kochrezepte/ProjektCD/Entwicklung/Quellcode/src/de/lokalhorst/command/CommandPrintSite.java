package de.lokalhorst.command;

import java.text.ParseException;
import de.lokalhorst.helper.BuyList;
import de.lokalhorst.helper.RecipeHelper;

/**
 * Klasse zur Erstellung einer druckbaren Ansicht für ein Rezept oder die Einkaufsliste
 * @author Christoph Antes
 * @version 06.11.2010
 *	
 */
public class CommandPrintSite extends Command
{
   
	/**
	 * printRecipe splittet die übermittelten Parameter des Input-Objektes auf 
	 * und erzeugt daraus ein vollständiges RecipeHelper-Objekt, welches gedruckt werden soll
	 * 
	 * @param input
	 * 		CommandInput-Objekt
	 * 
	 * @return output
	 * 		CommandOutput-Objekt
	 * @throws ParseException
	 */
    private CommandOutput printRecipe(CommandInput input) throws ParseException
    {
	CommandOutput out = new CommandOutput();
		
	//Stringarrays für Rezeptattribute, Zutaten und Zubereitungsschritte erstellen. Trennzeichen ist "_"
	String[] recipeattributes = input.getParam("recipeattributes").split("_");
	String[] ingredients = input.getParam("ingredients").split("_");
	String[] prepsteps = input.getParam("prepsteps").split("_");
		
	//RecipeHelper Objekt mit den oben erstellten Stringarrays erzeugen
	RecipeHelper recipe = new RecipeHelper(recipeattributes, ingredients, prepsteps);
		
		
	out.addValue("recipe", recipe);
		
	out.setTargetView("PrintRecipe");
	return out;
    }
    
    
    
    
    
    /**
     * printBuyingList holt die aktuell bestehende Einkaufsliste aus der Session und
     * fügt sie zum Output-Objekt hinzu
     * 
     * @param input
     * 			CommandInput-Objekt
     * 
     * @return	output
     * 			CommandOutput-Objekt
     */
    private CommandOutput printBuyinglist(CommandInput input)
    {
	CommandOutput out = new CommandOutput();

	// Einkaufsliste aus der Session holen
	BuyList buyinglist = (BuyList)(input.getSession().getAttribute("buylist"));
	
	//Speichern der Einkaufsliste und der Summation im Outputobjekt
	out.addValue("buylist", buyinglist.getBuylist());
	out.addValue("summation", buyinglist.getSummap());
		
	
	out.setTargetView("PrintBuyList");
	return out;
    }
    
    
    @Override
    public CommandOutput execute(CommandInput input) throws ParseException
    {
	//Ergebnisobjekt anlegen
	CommandOutput output = new CommandOutput();
		
	//1 = Rezept drucken
	//2 = Einkaufsliste drucken
	int choice = Integer.parseInt(input.getParam("param"));
	
	switch(choice)
	{
		case 1: output = printRecipe(input);break;
		case 2: output = printBuyinglist(input);break;
		default:break;
	}
		
	return output;
    }

}
