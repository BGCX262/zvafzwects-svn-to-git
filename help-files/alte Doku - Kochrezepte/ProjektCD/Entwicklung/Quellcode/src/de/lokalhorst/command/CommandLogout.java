package de.lokalhorst.command;

import de.lokalhorst.db.dto.CookDTO;

/**
 * Logout vom System
 * 
 * @author Jochen Pätzold, Christian Zöllner
 * @version 10.11.10
 * 
 */
public class CommandLogout extends Command
{
    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();

	if (input.getSession().getAttribute("cook") == null) {
		output.setErrorMsg("Illegaler Seitenaufruf von \"Logout\" ohne Anmeldung!");
		output.setTargetView("Default");
		return output;
	}
	
	CookDTO cook = (CookDTO) input.getSession().getAttribute("cook");
	output.setInfoMsg(cook.getCook_name() + " hat die Küche verlassen...");
	
	input.getSession().invalidate(); //session löschen
	
	output.setTargetView("Default");

	return output;
    }

}
