package dbklassen;

public class DBHobbies
{
	private int hnr;
	private String hobby;
	
	
	/**
	 * @param hnr
	 * @param hobby
	 */
	public DBHobbies(String hobby) {
		super();
		this.hobby = hobby;
	}
	public DBHobbies() {
		// TODO Auto-generated constructor stub
	}
	
	/**
	 * @return the hobby
	 */
	public String getHobby()
	{
		return hobby;
	}
	/**
	 * @param hobby the hobby to set
	 */
	public void setHobby(String hobby )
	{
		this.hobby = hobby;
	}
	/**
	 * @return the hnr
	 */
	public int getHnr()
	{
		return hnr;
	}
	
	public void setHnr(int h)
	{
		this.hnr = h;
	}
	
	public void printInfo()
	{
		System.out.print(this.hnr + "  " +this.hobby+"\n");
	}
}
