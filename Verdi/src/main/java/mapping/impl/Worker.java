package mapping.impl;

public class Worker implements CharSequence
{
	private final String name;
	private final String prename;
	private final String department;
	private Building building;
	private int id;
	private boolean ismember;
	

	public Worker(String name, String prename, String department) 
	{
		this.name = name.trim();
		this.prename = prename.trim();
		this.department = department.trim();
		this.id = (name.toLowerCase() + prename.toLowerCase()).hashCode();
		
//		System.out.println(this.toString() + " = " + id);
	}

	public String getName() 
	{
		return name;
	}

	public String getPrename() 
	{
		return prename;
	}

	public String getDepartment() 
	{
	    return department;
	}

	public Building getBuilding() 
	{
		return building;
	}

	public void setBuilding(Building house)
    {
        this.building = house;
    }

	public int getId() 
	{
		return id;
	}

	public boolean ismember() 
	{
		return ismember;
	}
	public void setmember()
	{
		this.ismember = true; 
	}

	@SuppressWarnings("unused")
    private static final int stringtoRoom(String raum) 
	{
		try
		{
			return Integer.parseInt(raum);
		}
		catch(NumberFormatException e)
		{
			return -1;
		}
	}

	private static final String translatetoboolean(boolean bool)
	{
	    if(bool)
	    {
	        return "ja";
	    }
	    else
	    {
	        return "nein";
	    }
	}
	
	@Override
    public String toString()
	{
		return String.format("%s;%s;%s;%s.%s.%s;%s", name, prename, department, " " + translatetoboolean(ismember));
	}
	
	@Override
	public int length() 
	{
		return this.toString().length();
	}

	@Override
	public char charAt(int index) 
	{
		return this.toString().charAt(index);
	}

	@Override
	public CharSequence subSequence(int start, int end) 
	{
		return this.toString().subSequence(start, end);
	}

}
