package de.lokalhorst.command;

import java.sql.Connection;

import de.lokalhorst.db.ConnectDB;
import de.lokalhorst.db.CookDB;
import de.lokalhorst.db.dto.CookDTO;
import de.lokalhorst.helper.EncodePwd;

/**
 * Geheime Frage um das Passwort neu zu setzen
 * 
 * @author Jochen Pätzold
 * @version 28.10.10
 * 
 */
public class CommandSecretQuestion extends Command
{
    private String error_msg = null; // Fehlermeldungen der Eingabekontrolle
    private final String success_msg = "Passwort erfolgreich geändert. Sie sind nun einloggt!";

    @Override
    public CommandOutput execute(CommandInput input) throws Exception
    {
	CommandOutput output = new CommandOutput();

	// Lesen der Formulardaten
	String answer = input.getParam("answer").trim();
	String password = input.getParam("password").trim();
	String password_2 = input.getParam("password_2").trim();
	// Lesen der zuvor in der Session hinterlegten CookDTO
	CookDTO sec_cook = (CookDTO) input.getSession()
		.getAttribute("sec_cook");

	// Formularwerte durchschleifen
	output.addValue("answer", answer);
	output.addValue("password", password);
	output.addValue("password_2", password_2);

	// Prüfung, ob Eingaben regelkonform sind
	String sec_answer = sec_cook.getSec_answer();
	checkForm(answer, sec_answer, password, password_2);

	if (error_msg != null)
	{ // Eingabefehler aufgetreten

	    output.setInfoMsg(null); // Hinweis rücksetzen
	    output.setErrorMsg(error_msg); // Fehlermeldung setzen

	    output.setTargetView("SecretQuestion"); // Nachfolgeseite
	} else
	{ // Antwort richtig und Passwort OK
	    int cook_id = sec_cook.getCook_id();
	    String encPdw = EncodePwd.encode(password);

	    Connection cn = ConnectDB.getPoolConnection(true);
	    boolean success = CookDB.changePassword(cn, cook_id, encPdw);
	    ConnectDB.closeConnection(cn);

	    if (success)
	    { // Passwort geändert
	      // Anmelden und Entfernen der sec_cook CookDTO aus der Session
		sec_cook.setPwd(encPdw);
		input.getSession().setAttribute("cook", sec_cook);
		input.getSession().removeAttribute("sec_cook");
		output.setInfoMsg(success_msg); // Hinweis setzen
		output.setErrorMsg(null); // Fehlermeldung rücksetzen
		output.setTargetView("Default"); // Nachfolgeseite
	    } else
	    { // Änderung in DB war nicht erfolgreich
		output.setInfoMsg(null); // Hinweis setzen
		output.setErrorMsg("Aktualisierung fehlgeschlagen, bitte probieren Sie es erneut!"); // Fehlermeldung
												     // rücksetzen
		output.setTargetView("SecretQuestion"); // Nachfolgeseite

	    }

	}

	return output;
    }

    /**
     * checkForm überprüft die Gültigkeit der Eingabefelder Antwort und Passwort
     * aus dem Passwort-vergessen-Formular
     * 
     * @param answer
     *            Antwort aus dem Formular
     * @param sec_answer
     *            Antwort aus dem Datenbank
     * @param password
     *            neues Passwort das registiert werden soll
     * @param password_2
     *            neues Passwort das registiert werden soll
     */
    private void checkForm(String answer, String sec_answer, String password,
	    String password_2)
    {
	error_msg = null;
	// answer
	if (!answer.equals(sec_answer))
	    error_msg = "Die geheime Antwort ist falsch!";
	// password
	else if (password.length() < 5)
	    error_msg = "Das Paswort enthält weniger als 5 Zeichen!";
	else if (password.length() > 30)
	    error_msg = "Das Paswort enthält mehr als 30 Zeichen!";
	else if (!password.equals(password_2))
	    error_msg = "Passwort und Wiederholung sind nicht identisch!";

	return;
    }

}
