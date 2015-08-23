package dbklassen;

public class Sequence {
	private int nextNumber;
	private String seqname;
	
	public Sequence(int startvalue, String name) 
	{
		nextNumber = startvalue;
		seqname = name;
	}
	
	public int nextVal() 
	{
		return nextNumber++;
	}
	
	public int currVal() 
	{
		return nextNumber;
	}
	
	public String getName() 
	{
		return this.seqname;
	}
	
	public void print()
	{
		System.out.println(this.getName() + " " + this.currVal());
	}
}