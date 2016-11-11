package trinadh.app.findgbt;

public class Route1 {
	
	private String[] stops;
	private String[] towards;
	
	Route1()
	{
		
		towards =new String[2];
		
		towards[0]="Dock Shopping Center";
		towards[1]="Broad St. & university Ave";
		
	}
	
	public String[] getTowards()
	{
		
	return towards;	
	}

}
