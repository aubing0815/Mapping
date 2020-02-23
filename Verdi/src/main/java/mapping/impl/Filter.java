package mapping.impl;

public interface Filter 
{
	WorkerContainer<Integer, Worker> filterWorkers(WorkerContainer<Integer, Worker> workers);
}
