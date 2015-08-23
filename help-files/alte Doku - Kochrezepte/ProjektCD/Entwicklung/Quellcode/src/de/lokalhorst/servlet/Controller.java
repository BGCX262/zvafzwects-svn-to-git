package de.lokalhorst.servlet;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import de.lokalhorst.command.*;

/**
 * Angedachter Controller, der mit dem CommandPattern arbeitet, Dummieumleiung
 * 
 * @author Christian Zöllner
 * @version 08.11.2010
 */
public class Controller extends HttpServlet
{
    private static final long serialVersionUID = 1L;

    /**
     * @see HttpServlet#HttpServlet()
     */
    public Controller()
    {
	super();
    }

    /**
     * @see HttpServlet#service(HttpServletRequest request, HttpServletResponse
     *      response)
     */
    protected void service(HttpServletRequest request,
	    HttpServletResponse response) throws ServletException, IOException
    {
	// Über den Broker werden die Aktionen aufgerufen, bzw die passenden
	// Commands
	CommandBroker broker = CommandBroker.getInstance();

	// Wichtig für durchgängige UTF-8 Kodierung!
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");

	// Die auszuführende Aktion aus der jsp, also der Name des Commands
	String action = request.getParameter("action");
	// Kapselt die Eingabedaten für das Command
	CommandInput in = new CommandInput(request);
	// Kapselt die Ausgabe eines Commands
	CommandOutput out = null;

	try
	{
	    if (action != null)
	    {
		// Hier wird die gewählte Action aus dem Request Objekt der JSP
		// ausgeführt.
		// Es wird immer ein CommandOutput Objekt zurückgegeben
		out = broker.lookUp(action).execute(in);

		// Resultate der Action als Attribut ans Requestobjekt
		// übergeben
		request.setAttribute("out", out.getResults());
		// Durchreichen der Info-Meldung
		request.setAttribute("InfoMsg", out.getInfoMsg());
		// Durchreichen der Fehlermeldung
		request.setAttribute("ErrorMsg", out.getErrorMsg());
		// Forwarden zur JSP. Welche JSP das ist, steht im
		// Outputobjekt
		request.getRequestDispatcher(out.getTargetView() + ".jsp")
			.forward(request, response);

	    } else
	    { // Umleitung auf Startseite wenn keine Action angegeben wird
		request.getRequestDispatcher("Default.jsp").forward(request,
			response);
	    }
	} catch (Exception e)
	{
	    System.err.println("Fehler beim Actionaufruf:" + e);
	    e.printStackTrace();

	    // Verhalten im Fehlerfall, wenn es zb Action nicht gibt
	    request.setAttribute("ErrorMsg",
		    "Es ist ein Fehler aufgetreten, bitte erneut versuchen");

	    request.getRequestDispatcher("Default.jsp").forward(request,
		    response);
	}

    }

}
