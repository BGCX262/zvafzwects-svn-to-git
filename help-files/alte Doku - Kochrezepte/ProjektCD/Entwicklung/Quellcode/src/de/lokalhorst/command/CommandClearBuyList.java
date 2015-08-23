package de.lokalhorst.command;

import java.sql.Connection;

import de.lokalhorst.db.BuyListedDB;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.dto.CookDTO;
import de.lokalhorst.helper.BuyList;

/**
 * Klasse zum vollständigen leeren der Einkaufsliste, Ist man eingeloggt wird
 * sie auch in der Datenbank gelöscht
 * 
 * @author Christian Zöllner
 * @version 02.11.2010
 * 
 */
public class CommandClearBuyList extends Command
{
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();
	BuyList buylist = (BuyList) (input.getSession().getAttribute("buylist"));

	CookDTO cook = (CookDTO) input.getSession().getAttribute("cook");

	if (cook == null)
	{
	    buylist.clear(); // liste in session leeren
	} else
	{
	    Connection cn = ConnectDB.getPoolConnection(true);

	    // Liste aus DB löschen
	    BuyListedDB.clearBuylist(cn, cook.getCook_id());
	    ConnectDB.closeConnection(cn);

	    buylist.clear(); // liste in session leeren
	}

	output.setInfoMsg("Die Einkaufsliste wurde geleert");
	output.setTargetView("ShowBuyList");

	return output;
    }

}
