package mapping.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;
import java.util.TreeMap;
import java.util.TreeSet;
import java.util.stream.Collectors;

public class OrgchartGeneratorFiduciaGAD implements OrgChartGenerator
{
	
	private static OrgchartGeneratorFiduciaGAD orgchartgen = null;
	
	private OrgchartGeneratorFiduciaGAD() 
	{}
		
	public static OrgchartGeneratorFiduciaGAD getInstance()
	{
		if(orgchartgen == null)
		{
			orgchartgen = new OrgchartGeneratorFiduciaGAD();
		}
		return orgchartgen;
	}
	
	@Override
	public Map<String, List<String>> generateOrgChartbyWorkers(WorkerContainer<Integer, Worker> workers)
    {
		Map<String, List<String>> orgchart = new TreeMap<>();
		
		List<String> cheat = new ArrayList<String>();
		cheat.add("FiduciaGAD");
		
        SortedSet<String> departmentset = new TreeSet<>();
        SortedSet<Section> sectionset = new TreeSet<>();

        workers.values().stream().forEach(worker -> departmentset.add(worker.getDepartment()));
        departmentset.stream().forEach(departement -> sectionset.add(Section.getSectionfromDepartment(departement)));

        orgchart.put("FiduciaGAD", sectionset.stream().map(section -> section.name()).collect(Collectors.toList()));
        orgchart.put("Unternehmen", cheat);
        sectionset.forEach(section -> orgchart.put(section.name(), new ArrayList<String>()));
        departmentset.removeIf(departement -> departmentset.contains(Section.getSectionfromDepartment(departement).name()));
        departmentset.forEach(departement -> orgchart.get(Section.getSectionfromDepartment(departement).name()).add(departement));
        
        return orgchart;
    }
	
    
	enum  Section
	{
		INVALID(-1),
		FiduciaGAD(0),
		VPB(1),
		GPS(2),
		PEM(3),
		SPB(4),
		FAM(5),
		PPM(6),
		BQF(7),
		BTO(8),
		KMV(9),
		KSE(10),
		VVR(11),
		ITS(12),
		ARI(13),
		ZEK(14),
		MFF(15),
		ITG(16),
		VVM(17),
		ZSP(18);
	
		private final int value;
		
		private Section(int value)
		{
			this.value = value;
		}
		
		public int getValue()
		{
			return value;
		}
		
		static Section getSectionfromDepartment(String department)
		{
			return stringtoSection(department.substring(0, 3));
		}
		
		static final Section stringtoSection(String section) 
		{
			try
			{
				return Section.valueOf(section);
			}
			catch(IllegalArgumentException e)
			{
			    // Bereich ist nicht in der Auswahl zu finden
			}
			return Section.INVALID;
		}
	}
}
