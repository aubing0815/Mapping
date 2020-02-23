package mapping.impl;

import java.util.List;
import java.util.Map;

public interface OrgChartGenerator 
{
	Map<String, List<String>> generateOrgChartbyWorkers(WorkerContainer<Integer, Worker> workers);
}
