package de.lokalhorst.helper;

import java.security.MessageDigest;

/**
 * Klasse zum Erstellen eines md5 Hash
 * 
 * 
 * @author Christian Zöllner
 * @version 29.10.2010
 */

public class EncodePwd
{

    /**
     * Methode berechnet md5 Hash zum übergebenen Passwortstring
     * 
     * @param pwd
     *            Passwortstring
     * @return md5 
     * 		  Hash als String
     * 
     * @exception Gibt null zürück
     */
    public static String encode(String pwd)
    {
	StringBuilder hash = new StringBuilder();

	try
	{
	    MessageDigest md = MessageDigest.getInstance("MD5");
	    
	    // Hash erstellen
	    byte[] hash_buffer = md.digest(pwd.getBytes()); 

	    for (int i = 0; i < hash_buffer.length; i++)
	    {
		// < 16 weil toHexString sonst bei 0x03 usw die führende 0
		// abschneidet, gibt dann nur 3 statt 03 aus
		if ((0xff & hash_buffer[i]) < 16)
		{
		    hash.append("0");
		}
		hash.append(Integer.toHexString(0xff & hash_buffer[i]));
	    }

	} catch (Exception e)
	{
	    System.err.println("Fehler bei Verschlüßelung :" + e);

	    return null;
	}

	return hash.toString();

    }
}
