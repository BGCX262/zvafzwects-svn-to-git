package de.lokalhorst.command;

import de.lokalhorst.db.dto.CookDTO;

/**
 * Command zur Weiterleitung ohne den Controller zu umgehen.
 * Es leitet zur View aus dem Parameter "targeview" weiter.
 * 
 * @author Jochen Pätzold, Christoph Antes
 * @version 22.10.2010
 */
public class CommandRedirect extends Command
{

    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();
	
	
	//Prüfen, ob der aktuelle Nutzer Admin ist
	if(input.getParam("targetview").equals("AdminFunctions")) //wenn der User die Adminfunktionen aufrufen will
	{
	    CookDTO cook = (CookDTO) input.getSession().getAttribute("cook");

	    if(cook==null) //Ist der User nicht eingeloggt?
	    {
		output.setInfoMsg("Keine Berechtigung! Sie sind nicht als Administrator eingeloggt.");
		output.setTargetView("Default");
	    }
	    else if(!cook.isIs_admin()) //ist der User eingeloggt, aber kein Admin?
	    {
		output.setInfoMsg("Keine Berechtigung! Sie sind nicht als Administrator eingeloggt.");
		output.setTargetView("Default");
	    }
	    else
	    {
		//  Extraktion der TargetView aus dem CommandInput
		output.setTargetView(input.getParam("targetview"));
	    }
	}
	else //Alle anderen Seitenredirects, die nichts mit den Adminfunktionen zu tun haben
	{
	    output.setTargetView(input.getParam("targetview"));
	}

	

	return output;
    }

}
