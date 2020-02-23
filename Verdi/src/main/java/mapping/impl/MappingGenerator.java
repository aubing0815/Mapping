package mapping.impl;

import java.io.BufferedWriter;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;


public class MappingGenerator
{
    private static Path workerspath;
    private static Path memberspath;
    private static Path destinationpath ;

    private WorkerContainer<Integer, Worker> workers = new WorkerContainer<>();
    private List<Integer> members = new ArrayList<>();
    
    private static OrgChartGenerator orgchartgenerator;
    private static Filter filter;
    
    private Map<String, List<String>> orgchart;
    private List<String> mapping;

    static 
    {
    	try 
    	{
    		ClassLoader loader = MappingGenerator.class.getClassLoader();
    		workerspath = Paths.get(loader.getResource("Mitarbeiterliste.csv").toURI());
    		memberspath = Paths.get(loader.getResource("Mitgliederliste.csv").toURI());
    		destinationpath = Paths.get(loader.getResource("Mapping.csv").toURI());
    	} 
    	catch (URISyntaxException e) 
    	{
    		e.printStackTrace();
    	}
    }

    public MappingGenerator(OrgChartGenerator orgchartgen, Filter filter)
    {
    	MappingGenerator.orgchartgenerator = orgchartgen;
    	MappingGenerator.filter = filter;
    }
    

    public static void main(String[] args)
    {

    	MappingGenerator generator = new MappingGenerator(OrgchartGeneratorFiduciaGAD.getInstance(),
    														FilterFiduciaGAD.getInstance());
    	generator.generate();
    }

    private void generate() 
    {
        workers = readWorkers(workers);
        System.out.println("_____________________________________________________________________________");
        members = readMembers(members);
        workers = filter.filterWorkers(workers);
        workers = mapMemberstoWorkers(members, workers);
        
        orgchart = orgchartgenerator.generateOrgChartbyWorkers(workers);
        
        mapping = initializeMapping(mapping);
        mapping = addWorkerstoFile(mapping, workers);
        mapping = addOrgCharttoFile(mapping, orgchart);
        
        writeMappingtoFile(mapping);
	}

	private static List<String> initializeMapping(List<String> mapping) 
    {
		mapping = new ArrayList<String>();
		mapping.add("ID; Name; Mitglied; Typ; ID; zugeordnet zu");
		
		return mapping;
	}

	private static List<String> addWorkerstoFile(List<String> mapping, WorkerContainer<Integer, Worker> workers)
    { 
        String workerformat = "%s;%s %s;%s;Mitarbeiter;%s;%s";
        workers.values().stream().forEach(worker -> mapping.add(String.format(workerformat,
        																	worker.getId(),
        																	worker.getPrename(),
        																	worker.getName(),
        																	worker.ismember(),
        																	worker.getId(),
        																	worker.getDepartment().hashCode())));

        return mapping;
    }
    
    private static List<String> addOrgCharttoFile(List<String> mapping, Map<String, List<String>> orgchart)
    {
        String organisationentityformat = "%s;%s;;Organisationseinheit;%s;%s";
        orgchart.entrySet().stream().forEach(section -> 
        {
            section.getValue().stream().forEach(
                departement -> mapping.add(String.format(organisationentityformat,
                											departement.hashCode(),
                											departement,
                											departement.hashCode(),
                											section.getKey().hashCode())));
        });

        return mapping;
    }


    private void writeMappingtoFile(List<String> list)
    {
        try
        {
            BufferedWriter writer = Files.newBufferedWriter(destinationpath, StandardCharsets.UTF_8);

            list.stream().forEach(s -> 
            {
                try
                {
                    writer.newLine();
                    writer.write(s);
                }
                catch (IOException e)
                {
                    e.printStackTrace();
                }
            });
            writer.flush();
            writer.close();
            
        }
        catch (IOException e1)
        {
            e1.printStackTrace();
        }
    }


    private WorkerContainer<Integer, Worker> readWorkers(WorkerContainer<Integer, Worker> workers)
    {
    	
//        email = email.replaceAll("�", "ae");
//        email = email.replaceAll("�", "ue");
//        email = email.replaceAll("�", "oe");
//        email = email.replaceAll("�", "ss");
        try
        {
            List<String> lines;

            lines = Files.readAllLines(workerspath, Charset.defaultCharset());

            for (String line : lines)
            {
                Worker w = stringtoWorker(line);
                
                System.out.println(w.getName().toLowerCase() + " + " + w.getPrename().toLowerCase() + " = " + w.getId());
                
                workers.put(w.getId(), w);
            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        return workers;
    }
    private Worker stringtoWorker(String line)
    {
        String name;
        String prename;
        String department;

        String[] splited = line.split(";");

        name = splited[0];
        prename = splited[1];
        department = splited[2];

        Worker worker = new Worker(name, prename, department);

        //		try
        //		{
        //			String[] pos = splited[4].split("\\.");
        //			Optional<Integer> house = Optional.of(Integer.parseInt(pos[0]));
        //			Optional<String> floor = Optional.of(pos[1]);
        //			Optional<Integer> room = Optional.of(Integer.parseInt(pos[2]));
        //			
        //			house.ifPresent(h -> worker.setHouse(House.valueOf(h)));
        //			floor.ifPresent(f -> worker.setFloor(Floor.valueOf(f)));
        //			room.ifPresent(r -> worker.setRoom(r));
        //			
        //		}
        //		catch(IndexOutOfBoundsException e)
        //		{
        //			// ist egal
        //		}

        return worker;
    }

    @SuppressWarnings("unused")
    private List<Worker> sortWorkers(Comparator<Worker> comparator, WorkerContainer<Integer, Worker> workers)
    {
        return workers.toList().stream().sorted(comparator).collect(Collectors.toList());
    }

    @SuppressWarnings("unused")
    private void writeWorkers(List<Worker> workers)
    {
        try
        {
            Files.write(destinationpath, workers, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    private List<Integer> readMembers(List<Integer> members)
    {
        try
        {
            List<String> lines;

            lines = Files.readAllLines(memberspath, Charset.defaultCharset());


            members.addAll(lines.stream().mapToInt(s -> 
            {
                String[] splitted = s.trim().split("\\;");
                String name = splitted[0];
                String vorname = splitted[1];

                System.out.println(name.toLowerCase() + " + " + vorname.toLowerCase() + " = " + (name.toLowerCase() + vorname.toLowerCase()).hashCode());
                return (name.toLowerCase() + vorname.toLowerCase()).hashCode();

            }).boxed().collect(Collectors.toSet()));

        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        
       return members;
    }

    private WorkerContainer<Integer, Worker> mapMemberstoWorkers(List<Integer> members, WorkerContainer<Integer, Worker> workers)
    {
            members.stream().forEach(m -> 
            {
                int x = m;
                try
                {
                    workers.get(m).setmember();
                }
                catch(NullPointerException e)
                {
                    //	        verdi  mitglied aber nicht in Personaldatenbank
                    System.out.println(x);
                }
            }
            );
            return workers;
    }
    
}
