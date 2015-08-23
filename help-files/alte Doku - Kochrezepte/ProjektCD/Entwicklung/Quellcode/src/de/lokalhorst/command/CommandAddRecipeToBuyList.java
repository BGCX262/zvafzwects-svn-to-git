package de.lokalhorst.command;

import de.lokalhorst.helper.BuyList;
import de.lokalhorst.helper.BuyListEntry;
import de.lokalhorst.helper.IngredientHelper;

/**
 * Klasse zum hinzufügen, des Namens und der Zutaten eines Rezeptes, auf die
 * Einkaufsliste
 * 
 * @author Christian Zöllner
 * @version 01.11.2010
 */

public class CommandAddRecipeToBuyList extends Command
{

    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();

	// Einkaufsliste aus der Session holen
	BuyList BList = (BuyList) (input.getSession().getAttribute("buylist"));

	// Einkaufsliste anlegen, falls es noch keine gab
	if (BList == null)
	{
	    BList = new BuyList();
	    input.getSession().setAttribute("buylist", BList);
	}

	// Rezept (Einkaufslisteneinträge) auslesen, ein String für alle Zutaten
	// und Zutateattribute, Trenner ist _
	String buylistentries = input.getParam("buylistentries");

	BuyListEntry entry = new BuyListEntry();
	String[] results = buylistentries.split("_");

	// Prüfung ob Rezept Zutaten hat
	if (results.length > 1)
	{
	    entry.setR_name((String) input.getSession().getAttribute(
		    "recp_name_bl")); // Rezeptname
	    entry.setRecp_id((Integer) input.getSession().getAttribute(
		    "recp_id_bl"));// Rezept id
	    entry.setQuantity((Integer) input.getSession().getAttribute(
		    "quantity"));// Personenzahl

	    System.err.println(entry.getRecp_id() + ", " + entry.getQuantity());

	    for (int i = 0; i < results.length; i += 3)
	    {
		// Zutatenobjekte anlegen
		entry.AddIngredients(new IngredientHelper(Integer
			.parseInt(results[i]), results[i + 1], results[i + 2]));
	    }
	    // Hinzufügen der Rezepte (Einkaufslisteneinträge)
	    int erg = BList.add(entry);

	    if (erg == 1)
	    {
		output.setErrorMsg("Ihre Einkaufsliste ist voll");
	    }
	    if (erg == -1)
	    {
		output.setErrorMsg("Einfügen fehlgeschlagen");
	    }
	    if (erg == 0)
	    {
		output.setInfoMsg("Rezept in die Einkaufsliste eingetragen");
	    }
	} else
	// Rezept hat keine Zutaten
	{
	    output.setErrorMsg("Das gewählte Rezept besitzt keine Zutaten");
	}
	output.addValue("buylist", BList.getBuylist());
	output.addValue("summation", BList.getSummap());

	output.setTargetView("ShowBuyList");

	return output;
    }
}
