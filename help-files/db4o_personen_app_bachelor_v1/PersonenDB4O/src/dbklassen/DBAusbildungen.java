package dbklassen;

import java.util.Date;

public class DBAusbildungen
{
	private int anr;
	private String beschreibung;
	private Date von;
	private Date bis;
	private String abschluss;
	private Date datum;
	
	/**
	 * @param anr
	 * @param beschreibung
	 * @param von2
	 * @param bis2
	 * @param abschluss
	 * @param datum2
	 */
	public DBAusbildungen(String beschreibung, Date von2, java.util.Date bis2,
			String abschluss, Date datum2) {
		super();
		this.beschreibung = beschreibung;
		this.von = von2;
		this.bis = bis2;
		this.abschluss = abschluss;
		this.datum = datum2;
	}
	public DBAusbildungen() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the beschreibung
	 */
	public String getBeschreibung()
	{
		return beschreibung;
	}
	/**
	 * @param beschreibung the beschreibung to set
	 */
	public void setBeschreibung(String beschreibung )
	{
		this.beschreibung = beschreibung;
	}
	/**
	 * @return the von
	 */
	public Date getVon()
	{
		return von;
	}
	/**
	 * @param von the von to set
	 */
	public void setVon(Date von )
	{
		this.von = von;
	}
	/**
	 * @return the bis
	 */
	public Date getBis()
	{
		return bis;
	}
	/**
	 * @param bis the bis to set
	 */
	public void setBis(Date bis )
	{
		this.bis = bis;
	}
	/**
	 * @return the abschluss
	 */
	public String getAbschluss()
	{
		return abschluss;
	}
	/**
	 * @param abschluss the abschluss to set
	 */
	public void setAbschluss(String abschluss )
	{
		this.abschluss = abschluss;
	}
	/**
	 * @return the datum
	 */
	public Date getDatum()
	{
		return datum;
	}
	/**
	 * @param datum the datum to set
	 */
	public void setDatum(Date datum )
	{
		this.datum = datum;
	}
	/**
	 * @return the anr
	 */
	public int getAnr()
	{
		return anr;
	}
	public void setAnr(int a)
	{
		this.anr = a;
	}
	
	public void printInfo()
	{
		System.out.print(this.anr + "  " +this.beschreibung+"  "+this.von+"  "+this.bis+"  "
				+this.abschluss+"  "+this.datum+"\n");
	}
}
