package mapping.impl;
import java.util.Comparator;

public class DepartmentComparator implements Comparator<Worker>
{
	private static DepartmentComparator comparator = null;
		
	private DepartmentComparator() 
	{}
		
	public static DepartmentComparator getInstance()
	{
		if(comparator == null)
		{
			comparator = new DepartmentComparator();
		}
		return comparator;
	}
		
	@Override
	public int compare(Worker m1, Worker m2) 
	{
		int section = OrgchartGeneratorFiduciaGAD.Section.getSectionfromDepartment(m1.getDepartment()).getValue() - OrgchartGeneratorFiduciaGAD.Section.getSectionfromDepartment(m2.getDepartment()).getValue();
		
		if(section == 0)
		{
			return m1.getDepartment().compareTo(m2.getDepartment());
		}
		else
		{
			return section;
		}
		
	}
}