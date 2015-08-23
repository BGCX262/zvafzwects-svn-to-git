package dbklassen;

import java.util.Date;

public class DBBerufe
{
	private int bNr;
	private String beschreibung;
	private Date begonnen;
	private Date beendet;
	private double gehalt;
	
	/**
	 * @param bNr
	 * @param beschreibung
	 * @param begonnen2
	 * @param beendet2
	 * @param gehalt
	 */
	public DBBerufe(String beschreibung, Date begonnen2, java.util.Date beendet2,
			double gehalt) {
		super();
		this.beschreibung = beschreibung;
		this.begonnen = begonnen2;
		this.beendet = beendet2;
		this.gehalt = gehalt;
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
	 * @return the begonnen
	 */
	public Date getBegonnen()
	{
		return begonnen;
	}
	/**
	 * @param begonnen the begonnen to set
	 */
	public void setBegonnen(Date begonnen )
	{
		this.begonnen = begonnen;
	}
	/**
	 * @return the beendet
	 */
	public Date getBeendet()
	{
		return beendet;
	}
	/**
	 * @param beendet the beendet to set
	 */
	public void setBeendet(Date beendet )
	{
		this.beendet = beendet;
	}
	/**
	 * @return the gehalt
	 */
	public double getGehalt()
	{
		return gehalt;
	}
	/**
	 * @param gehalt the gehalt to set
	 */
	public void setGehalt(double gehalt )
	{
		this.gehalt = gehalt;
	}
	/**
	 * @return the bNr
	 */
	public int getbNr()
	{
		return bNr;
	}
	
	public void setBnr(int b)
	{
		this.bNr = b;
	}
	
	public void printInfo()
	{
		System.out.print(this.bNr + "  " +this.beschreibung+"  "+this.begonnen+"  "+this.beendet+"  "
				+this.gehalt+" €\n");
	}
	
}
