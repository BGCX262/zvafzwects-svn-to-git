package de.lokalhorst.command;

import de.lokalhorst.helper.BuyList;

/**
 * Klasse zum Anzeigen der Einkaufsliste
 * 
 * @author Christian Zöllner
 * @version 02.11.2010
 */
public class CommandShowBuyList extends Command
{
    public CommandOutput execute(CommandInput input) throws Exception
    {

	CommandOutput output = new CommandOutput();
	// Einkaufsliste aus Session holen
	BuyList buylist = (BuyList) (input.getSession().getAttribute("buylist"));

	// Neue Einkaufsliste anlegen, wenn noch keine da ist
	if (buylist == null)
	{
	    buylist = new BuyList();
	    input.getSession().setAttribute("buylist", buylist);
	}

	if (buylist.isEmpty())
	{
	    output.setInfoMsg("Es befinden sich derzeit keine Rezepte in der Einkaufsliste");
	}

	output.addValue("buylist", buylist.getBuylist());
	output.addValue("summation", buylist.getSummap());
	output.setTargetView("ShowBuyList");

	return output;
    }
}
