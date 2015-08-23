package de.lokalhorst.db.dto;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * CookDTO DataTransferObjekt zur Tabelle Cook
 * 
 * @author Jacques Mwizerwa & Jochen Pätzold
 * @version 19.19.2010
 */

public class CookDTO
{
    private int cook_id;
    private String cook_name;
    private String pwd;
    private boolean is_admin;
    private String sec_question;
    private String sec_answer;

    /**
     * CookDTO Konstruktor mit Resultset als Parameter
     * 
     * @author Jochen Pätzold
     * @param rs
     *            Resultset
     */
    public CookDTO(ResultSet rs)
    {
	try
	{
	    cook_id = rs.getInt("cook_id");
	    cook_name = rs.getString("cook_name");
	    pwd = rs.getString("pwd");
	    is_admin = rs.getBoolean("is_admin");
	    sec_question = rs.getString("sec_question");
	    sec_answer = rs.getString("sec_answer");
	} catch (SQLException e)
	{
	    System.err.println("Ein DB-Fehler ist aufgetreten:" + e.toString());
	}
    }

    /**
     * CookDTO Konstruktor mit allen Parametern
     * 
     * @param cook_id
     * @param cook_name
     * @param pwd
     * @param is_admin
     * @param sec_question
     * @param sec_answer
     */
    public CookDTO(int cook_id, String cook_name, String pwd, boolean is_admin,
	    String sec_question, String sec_answer)
    {
	this.cook_id = cook_id;
	this.cook_name = cook_name;
	this.pwd = pwd;
	this.is_admin = is_admin;
	this.sec_question = sec_question;
	this.sec_answer = sec_answer;
    }

    /**
     * Standardkonstruktor
     */
    public CookDTO()
    {
	this.cook_id = 0;
	this.cook_name = "";
	this.pwd = "";
	this.is_admin = false;
	this.sec_question = "";
	this.sec_answer = "";
    }

    @Override
    public String toString()
    {
	return "CookDTO [cook_id=" + cook_id + ", cook_name=" + cook_name
		+ ", pwd=" + pwd + ", is_admin=" + is_admin + ", sec_question="
		+ sec_question + ", sec_answer=" + sec_answer + "]";
    }

    @Override
    public int hashCode()
    {
	final int prime = 31;
	int result = 1;
	result = prime * result + cook_id;
	result = prime * result
		+ ((cook_name == null) ? 0 : cook_name.hashCode());
	result = prime * result + (is_admin ? 1231 : 1237);
	result = prime * result + ((pwd == null) ? 0 : pwd.hashCode());
	result = prime * result
		+ ((sec_answer == null) ? 0 : sec_answer.hashCode());
	result = prime * result
		+ ((sec_question == null) ? 0 : sec_question.hashCode());
	return result;
    }

    @Override
    public boolean equals(Object obj)
    {
	if (this == obj)
	    return true;
	if (obj == null)
	    return false;
	if (getClass() != obj.getClass())
	    return false;
	CookDTO other = (CookDTO) obj;
	if (cook_id != other.cook_id)
	    return false;
	if (cook_name == null)
	{
	    if (other.cook_name != null)
		return false;
	} else if (!cook_name.equals(other.cook_name))
	    return false;
	if (is_admin != other.is_admin)
	    return false;
	if (pwd == null)
	{
	    if (other.pwd != null)
		return false;
	} else if (!pwd.equals(other.pwd))
	    return false;
	if (sec_answer == null)
	{
	    if (other.sec_answer != null)
		return false;
	} else if (!sec_answer.equals(other.sec_answer))
	    return false;
	if (sec_question == null)
	{
	    if (other.sec_question != null)
		return false;
	} else if (!sec_question.equals(other.sec_question))
	    return false;
	return true;
    }

    public int getCook_id()
    {
	return cook_id;
    }

    public void setCook_id(int cook_id)
    {
	this.cook_id = cook_id;
    }

    public String getCook_name()
    {
	return cook_name;
    }

    public void setCook_name(String cook_name)
    {
	this.cook_name = cook_name;
    }

    public String getPwd()
    {
	return pwd;
    }

    public void setPwd(String pwd)
    {
	this.pwd = pwd;
    }

    public boolean isIs_admin()
    {
	return is_admin;
    }

    public void setIs_admin(boolean is_admin)
    {
	this.is_admin = is_admin;
    }

    public String getSec_question()
    {
	return sec_question;
    }

    public void setSec_question(String sec_question)
    {
	this.sec_question = sec_question;
    }

    public String getSec_answer()
    {
	return sec_answer;
    }

    public void setSec_answer(String sec_answer)
    {
	this.sec_answer = sec_answer;
    }

}
