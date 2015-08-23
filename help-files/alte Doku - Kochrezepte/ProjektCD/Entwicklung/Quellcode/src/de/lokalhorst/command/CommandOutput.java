package de.lokalhorst.command;

import java.util.HashMap;

/**
 * Kapselt die Ausgaben eines Commands
 * 
 * @author Christian Zöllner
 * @version 21.10.2010
 */
public class CommandOutput
{

    /**
     * Resultate der SQL abfragen/ Infomeldungen, was man halt reinpacken will
     * ^^
     */
    private HashMap<String, Object> results;

    /** Name der Zielseite */
    private String targetView;

    /** Optionale Fehlermeldung */
    private String errorMsg;

    /** Optionale Infomeldung */
    private String infoMsg;

    public CommandOutput()
    {
	this.results = new HashMap<String, Object>();
	this.targetView = null;
    }

    public HashMap<String, Object> getResults()
    {

	return results;

    }

    public void setResults(HashMap<String, Object> results)
    {

	this.results = results;

    }

    public void addValue(String key, Object value)
    {

	this.results.put(key, value);

    }

    public String getTargetView()
    {

	return targetView;

    }

    public void setTargetView(String targetView)
    {

	this.targetView = targetView;

    }

    public String getErrorMsg()
    {

	return errorMsg;
    }

    public void setErrorMsg(String errorMsg)
    {

	this.errorMsg = errorMsg;
    }

    public String getInfoMsg()
    {

	return infoMsg;
    }

    public void setInfoMsg(String infoMsg)
    {

	this.infoMsg = infoMsg;
    }

}
