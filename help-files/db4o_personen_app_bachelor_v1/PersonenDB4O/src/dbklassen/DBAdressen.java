package dbklassen;

public class DBAdressen
{
	private int adrNr;
	private String plz;
	private String ort;
	private String str;
	private String typ;
	
	/**
	 * @param adrNr
	 * @param plz
	 * @param ort
	 * @param str
	 * @param typ
	 */
	public DBAdressen(String plz, String ort, String str, String typ) {
		super();
		this.plz = plz;
		this.ort = ort;
		this.str = str;
		this.typ = typ;
	}
	public DBAdressen() {
		// TODO Auto-generated constructor stub
	}
	/**
	 * @return the plz
	 */
	public String getPlz()
	{
		return plz;
	}
	/**
	 * @param plz the plz to set
	 */
	public void setPlz(String plz )
	{
		this.plz = plz;
	}
	/**
	 * @return the ort
	 */
	public String getOrt()
	{
		return ort;
	}
	/**
	 * @param ort the ort to set
	 */
	public void setOrt(String ort )
	{
		this.ort = ort;
	}
	/**
	 * @return the str
	 */
	public String getStr()
	{
		return str;
	}
	/**
	 * @param str the str to set
	 */
	public void setStr(String str )
	{
		this.str = str;
	}
	/**
	 * @return the typ
	 */
	public String getTyp()
	{
		return typ;
	}
	/**
	 * @param typ the typ to set
	 */
	public void setTyp(String typ )
	{
		this.typ = typ;
	}
	/**
	 * @return the adrNr
	 */
	public int getAdrNr()
	{
		return adrNr;
	}
	public void setAdrNr(int adr)
	{
		this.adrNr = adr;
	}
	
	public void printInfo()
	{
		System.out.print(this.adrNr + "  " +this.plz+"  "+this.ort+"  "+this.str+"  "+this.typ+"\n");
	}
	
}
