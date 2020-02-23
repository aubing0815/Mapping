package mapping.impl;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class WorkerContainer<K, V extends Worker> implements Map<K, Worker>
{
    private final Map<K, Worker> workers = new HashMap<>();
        
    public WorkerContainer()
    {
    }

    @Override
    public int size()
    {
        return workers.size();
    }

    @Override
    public boolean isEmpty()
    {
        return workers.isEmpty();
    }

    @Override
    public boolean containsKey(Object key)
    {
        return workers.containsKey(key);
    }

    @Override
    public boolean containsValue(Object value)
    {
        return workers.containsValue(value);
    }

    @Override
    public Worker get(Object key)
    {
        return workers.get(key);
    }

    @Override
    public Worker put(K key, Worker value)
    {
        return workers.put(key, value);
    }

    @Override
    public Worker remove(Object key)
    {
        return workers.remove(key);
    }

    @Override
    public void putAll(Map<? extends K, ? extends Worker> m)
    {
        workers.putAll(m);
    }

    @Override
    public void clear()
    {
        workers.clear();
    }

    @Override
    public Set<K> keySet()
    {
        return workers.keySet();
    }

    @Override
    public Collection<Worker> values()
    {
        return workers.values();
    }

    @Override
    public Set<java.util.Map.Entry<K, Worker>> entrySet()
    {
        return workers.entrySet();
    }
    
    public List<Worker> toList()
    {
        return new ArrayList<Worker>(workers.values());
    }


}
