package de.lokalhorst.db.dto;

/**
 * UnitDTO DataTransferObjekt zur Tabelle Unit
 * 
 * @author Jacques Mwizerwa
 * @version 19.10.2010
 */
public class UnitDTO
{
    private int unit_id;
    private String u_name;

    /**
     * Vollständiger Konstruktor
     * 
     * @param unit_id
     * @param u_name
     */
    public UnitDTO(int unit_id, String u_name)
    {
	super();
	this.unit_id = unit_id;
	this.u_name = u_name;
    }

    public int getUnit_id()
    {
	return unit_id;
    }

    public void setUnit_id(int unit_id)
    {
	this.unit_id = unit_id;
    }

    public String getU_name()
    {
	return u_name;
    }

    public void setU_name(String u_name)
    {
	this.u_name = u_name;
    }

    @Override
    public String toString()
    {
	return "UnitDTO [unit_id=" + unit_id + ", u_name=" + u_name + "]";
    }

}
