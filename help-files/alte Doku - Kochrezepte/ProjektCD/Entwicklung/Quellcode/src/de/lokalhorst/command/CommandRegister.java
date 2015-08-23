package de.lokalhorst.command;

import java.sql.Connection;
import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.CookDB;
import de.lokalhorst.db.dto.CookDTO;
import de.lokalhorst.helper.EncodePwd;

/**
 * Command zum Registrieren neuer Köche
 * 
 * @author Jochen Pätzold
 * @version 22.10.2010
 */
public class CommandRegister extends Command
{
    private String error_msg = null; // Fehlermeldungen der Eingabekontrolle
    private final String success_msg = "Herzlichen Glückwunsch, sie sind nun registriert. Sie könne sich oben rechts einloggen!";
    private final String failed_msg = "Registrierung fehlgeschlagen, bitte versuchen Sie es erneut.";

    @Override
    public CommandOutput execute(CommandInput input)
    {
	CommandOutput returnval = new CommandOutput(); // Rückgabeobjekt anlegen

	// Lesen der Formulardaten
	String name = ((String[]) input.getParams().get("newName"))[0];
	String password = ((String[]) input.getParams().get("newPassword"))[0];
	String password_2 = ((String[]) input.getParams().get("newPassword_2"))[0];
	String question = ((String[]) input.getParams().get("newQuestion"))[0];
	String answer = ((String[]) input.getParams().get("newAnswer"))[0];

	// Werte durchschleifen
	returnval.addValue("newName", name);
	returnval.addValue("newPassword", password);
	returnval.addValue("newPassword_2", password_2);
	returnval.addValue("newQuestion", question);
	returnval.addValue("newAnswer", answer);

	returnval.setTargetView("RegisterCook"); // Nachfolgeseite

	Connection cn = ConnectDB.getPoolConnection(true); // DB Verbindung

	// Prüfung, ob Eingaben regelkonform sind
	checkForm(cn, name, password, password_2, question, answer);

	if (error_msg != null)
	{ // Eingabefehler aufgetreten
	    returnval.setInfoMsg(null); // Hinweis rücksetzen
	    returnval.setErrorMsg(error_msg); // Fehlermeldung setzen
	} else
	{
	    // Eingabe okay, neuen Koch mit verschlüsseltem Passwort in
	    // Datenbank übernehemen
	    boolean successful = CookDB.registerNewCook(cn, new CookDTO(0,
		    name, EncodePwd.encode(password), false, question, answer));
	    if (successful)
	    { // Erfolgreich registriert
		returnval.setInfoMsg(success_msg); // Hinweismeldung setzen
		returnval.setErrorMsg(""); // Warnungmeldung setzen
		returnval.setTargetView("Default");
	    } else
	    { // Fehler bei Datenbankeintrag
		returnval.setInfoMsg(null); // Hinweis rücksetzen
		returnval.setErrorMsg(failed_msg); // Fehlermeldung setzen
	    }
	}
	ConnectDB.closeConnection(cn); // DB Verbindung schließen

	return returnval;
    }

    /**
     * checkForm überprüft die Gültigkeit der Eingabefelder Name, Passwort,
     * Frage und Antwort aus dem Registrier-Formular
     * 
     * @param cn
     *            Datenbankverbindung
     * @param name
     *            Name der registiert werden soll
     * @param password
     *            Passwort das registiert werden soll
     * @param question
     *            Geheime Frage die registiert werden soll
     * @param answer
     *            Geheime Antwort die registiert werden soll
     */
    private void checkForm(Connection cn, String name, String password,
	    String password_2, String question, String answer)
    {
	error_msg = null;
	// name
	if (name.length() < 5)
	    error_msg = "Der Name enthält weniger als 5 Zeichen!";
	else if (name.length() > 30)
	    error_msg = "Der Name enthält mehr als 30 Zeichen!";
	// password
	else if (password.length() < 5)
	    error_msg = "Das Paswort enthält weniger als 5 Zeichen!";
	else if (password.length() > 30)
	    error_msg = "Das Paswort enthält mehr als 30 Zeichen!";
	else if (!password.equals(password_2))
	    error_msg = "Passwort und Wiederholung sind nicht identisch!";
	// question
	else if (question.length() < 10)
	    error_msg = "Die Geheimfrage enthält weniger als 10 Zeichen!";
	else if (question.length() > 100)
	    error_msg = "Die Geheimfrage enthält mehr als 100 Zeichen!";
	// answer
	else if (answer.length() < 10)
	    error_msg = "Die geheime Antwort enthält weniger als 10 Zeichen!";
	else if (answer.length() > 100)
	    error_msg = "Die geheime Antwort enthält mehr als 100 Zeichen!";
	// DB-Abrage ob Name schon existiert
	else if (CookDB.existsName(cn, name))
	    error_msg = "Den Koch mit Namen \"" + name + "\" gibt es bereits!";

	return;
    }
}
