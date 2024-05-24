package se.laz.casual.statistics;

import se.laz.casual.statistics.entries.EntriesPerConnection;
import se.laz.casual.statistics.entries.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCallStatisticsStore
{
    private final Map<ServiceCallConnection, Map<ServiceCall, ServiceCallAccumulatedData>> DATA = new ConcurrentHashMap<>();
    public void store(ServiceCallConnection connection, ServiceCall serviceCall, ServiceCallData data)
    {
        DATA.compute(connection, (conn, accumulatedByServiceCall) -> {
            if (accumulatedByServiceCall == null)
            {
                accumulatedByServiceCall = new ConcurrentHashMap<>();
            }
            accumulatedByServiceCall.compute(serviceCall, (call, maybeCachedData) -> null == maybeCachedData ?  ServiceCallAccumulatedData.newBuilder().withServiceCallData(data).build() : maybeCachedData.accumulate(data));
            return accumulatedByServiceCall;
        });
    }
    public Optional<ServiceCallAccumulatedData> get(ServiceCallConnection connection, ServiceCall serviceCall)
    {
        Map<ServiceCall, ServiceCallAccumulatedData> accumulatedDataByServiceCall = DATA.get(connection);
        if(null == accumulatedDataByServiceCall)
        {
            return Optional.empty();
        }
        return Optional.ofNullable(accumulatedDataByServiceCall.get(serviceCall));
    }
    public List<EntriesPerConnection> get(ServiceCallConnection connection)
    {
        List<EntriesPerConnection> result = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        Map<ServiceCall, ServiceCallAccumulatedData> calls = DATA.get(connection);
        if(null != calls)
        {
            for (var entry : calls.entrySet())
            {
                entries.add(new Entry(entry.getKey(), entry.getValue()));
            }
        }
        if(!entries.isEmpty())
        {
            result.add(new EntriesPerConnection(connection, entries));
        }
        return result;
    }
    public List<EntriesPerConnection> getAll()
    {
        List<EntriesPerConnection> result = new ArrayList<>();
        DATA.keySet().forEach(item -> {
            List<EntriesPerConnection> items = get(item);
            result.addAll(items);
        });
        return result;
    }
}
