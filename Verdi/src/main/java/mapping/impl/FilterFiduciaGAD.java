package mapping.impl;

import mapping.impl.OrgchartGeneratorFiduciaGAD.Section;

public class FilterFiduciaGAD implements Filter
{
	private static FilterFiduciaGAD filter = null;
	
	private FilterFiduciaGAD()
	{}
	
	public static FilterFiduciaGAD getInstance()
	{
		if(filter == null)
		{
			filter = new FilterFiduciaGAD();
		}
		return filter;
	}
	
	@Override
	public WorkerContainer<Integer, Worker> filterWorkers(WorkerContainer<Integer, Worker> workers) 
	{
		workers.values().removeIf(m -> OrgchartGeneratorFiduciaGAD.Section.getSectionfromDepartment(m.getDepartment()).equals(Section.INVALID));
		return workers;
	}

}
