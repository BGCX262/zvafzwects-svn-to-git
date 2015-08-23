/**
 * 
 */
package de.lokalhorst.command;

import java.sql.Connection;

import de.lokalhorst.db.BuyListedDB;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.dto.CookDTO;
import de.lokalhorst.helper.BuyList;

/**
 * Klasse zum speichern, der Einkaufsliste in der Datenbank
 * 
 * @author Christian Zöllner
 * @version 09.11.2010
 */
public class CommandSaveBuyList extends Command
{

    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();
	CookDTO cook = (CookDTO) input.getSession().getAttribute("cook");
	BuyList buylist = (BuyList) input.getSession().getAttribute("buylist");

	if (cook == null)
	{
	    output.setInfoMsg("Sie müßen sich einlogen um ihre Einkaufsliste zu speichern");

	} else
	{
	    Connection cn = ConnectDB.getPoolConnection(true);

	    // Alte Einkaufsliste wird überschrieben
	    BuyListedDB.clearBuylist(cn, cook.getCook_id());
	    BuyListedDB
		    .saveBuyList(cn, cook.getCook_id(), buylist.getBuylist());
	    ConnectDB.closeConnection(cn);

	    output.setInfoMsg("Einkaufsliste gespeichert");

	}

	// mitgeben, sonst leere Seite
	output.addValue("buylist", buylist.getBuylist());
	output.addValue("summation", buylist.getSummap());

	output.setTargetView("ShowBuyList");

	return output;
    }

}
