package de.lokalhorst.command;

import java.sql.Connection;

import javax.servlet.http.HttpSession;

import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.CookDB;
import de.lokalhorst.db.dto.CookDTO;

/**
 * 
 * @author Christoph Antes
 * @version 10.11.2010
 */
public class CommandEditCook extends Command
{
    
    /**
     * Führt die Änderungen des Adminstatus durch
     * 
     * @param status
     * 		neuer Adminstatus
     * @param cook_id
     * @param cook_name
     * @return
     * 		CommandOutput-Objekt
     */
    private CommandOutput executeAdminModification(boolean status, int cook_id, String cook_name)
    {
	CommandOutput out = new CommandOutput();
	
	

	//Verbindung zur DB aufbauen
	Connection connect = ConnectDB.getPoolConnection(true);
    	      
	boolean success = CookDB.setAdminStatus(connect, status, cook_id);
    	    
	//DB-Verbindung schließen
	ConnectDB.closeConnection(connect); 
    	
    	
    	if(success)
    	{
    	    out.setInfoMsg("Änderungen erfolgreich übernommen! Neue Werte: \nCook-Name: "+cook_name+" || Cook-ID: "+cook_id+" || Adminstatus: "+status);
    	    out.setTargetView("AdminFunctions");
    	}
    	else
    	{
    	    out.setInfoMsg("Änderungen nicht erfolgreich! Bitte probieren Sie es erneut.");
    	    out.setTargetView("AdminFunctions");
    	}
    	
	return out;
    }
    
    
    
    
    
    
    
    
    
    
    
    @Override
    public CommandOutput execute( CommandInput input ) throws Exception 
    {
	//Ergebnisobjekt anlegen
    	CommandOutput output = new CommandOutput();
    	
    	boolean status = Boolean.parseBoolean(input.getParam("adminstatus"));
    	boolean adminstatus_old = Boolean.parseBoolean(input.getParam("adminstatus_old"));
    	int cook_id = Integer.parseInt(input.getParam("cook_id"));
    	String cook_name = input.getParam("cook_name");
    	
    	
    	//Eingeloggter User über die Session holen
    	HttpSession session = input.getSession();
    	CookDTO cook = (CookDTO)session.getAttribute("cook");
    	
    	
    	
    	if(cook.isIs_admin()) //Ist der User admin?
    	{
    	    if(cook_id != 1 && cook_id != cook.getCook_id()) //Wenn es sich bei dem zu ändernden User nicht um den eigenen- oder Chef-Account handelt
    	    {
    		if(status!=adminstatus_old) //Die Änderung wird nur ausgeführt, wenn sich der "neue" und der "alte" Status unterscheiden
    		{
    		    output = executeAdminModification(status, cook_id, cook_name);
    		}
    		else
    		{
    		    output.setInfoMsg("Keine Änderungen vorgenommen. Adminstatus war bereits \""+adminstatus_old+"\".");
    		    output.setTargetView("AdminFunctions");
    		}
    	    
    	    }
    	    else if(cook_id == 1) //Man versucht den Chefaccount zu ändern (Nur direkt über die URL möglich, da kein Änderungbutton vorhanden)
    	    {
    		output.setInfoMsg("Fehlgeschlagen! Keine Berechtigung den Chef Koch zu ändern.");
    		output.setTargetView("AdminFunctions");
    	    }
    	    else //Man versucht den eigenen Adminstatus zu ändern (Auch nur über URL möglich)
    	    {
    		output.setInfoMsg("Fehlgeschlagen! Sie dürfen ihren eigenen Adminstatus nicht ändern");
    		output.setTargetView("AdminFunctions");
    	    }
    	    
    	}
    	else //der User ist kein Admin
    	{
    	    output.setInfoMsg("Keine Berechtigung! Sie sind nicht als Administrator eingeloggt.");
	    output.setTargetView("Default");
    	}
    	
    	return output;
    }//execute()
	

}
