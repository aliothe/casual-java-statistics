package se.laz.casual.statistics;

import se.laz.casual.statistics.entries.EntriesPerConnection;
import se.laz.casual.statistics.entries.Entry;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCallStatistics
{
    private static final String CONNECTION_CAN_NOT_BE_NULL = "connection can not be null";
    private static final String SERVICE_CALL_CAN_NOT_BE_NULL = "serviceCall can not be null";
    private static final Map<ServiceCall, ServiceCallAccumulatedData> EMPTY_MAP = new ConcurrentHashMap<>();
    private static final Map<ServiceCallConnection, Map<ServiceCall, ServiceCallAccumulatedData>> DATA = new ConcurrentHashMap<>();
    private ServiceCallStatistics()
    {}
    public static void store(ServiceCallConnection connection, ServiceCall serviceCall, ServiceCallData data)
    {
        Objects.requireNonNull(connection, CONNECTION_CAN_NOT_BE_NULL);
        Objects.requireNonNull(serviceCall, SERVICE_CALL_CAN_NOT_BE_NULL);
        Objects.requireNonNull(data, "data can not be null");
        DATA.compute(connection, (conn, accumulatedByServiceCall) -> {
            if (accumulatedByServiceCall == null)
            {
                accumulatedByServiceCall = new ConcurrentHashMap<>();
            }
            accumulatedByServiceCall.compute(serviceCall, (call, maybeCachedData) -> null == maybeCachedData ?  ServiceCallAccumulatedData.newBuilder().withServiceCallData(data).build() : maybeCachedData.accumulate(data));
            return accumulatedByServiceCall;
        });
    }
    public static Optional<ServiceCallAccumulatedData> get(ServiceCallConnection connection, ServiceCall serviceCall)
    {
        Objects.requireNonNull(connection, CONNECTION_CAN_NOT_BE_NULL);
        Objects.requireNonNull(serviceCall, SERVICE_CALL_CAN_NOT_BE_NULL);
        Map<ServiceCall, ServiceCallAccumulatedData> accumulatedDataByServiceCall = DATA.get(connection);
        if(null == accumulatedDataByServiceCall)
        {
            return Optional.empty();
        }
        return Optional.ofNullable(accumulatedDataByServiceCall.get(serviceCall));
    }
    public static List<EntriesPerConnection> get(ServiceCallConnection connection)
    {
        Objects.requireNonNull(connection, CONNECTION_CAN_NOT_BE_NULL);
        List<EntriesPerConnection> result = new ArrayList<>();
        List<Entry> entries = new ArrayList<>();
        entries.addAll(Optional.ofNullable(DATA.get(connection))
                               .orElseGet(() -> EMPTY_MAP)
                               .entrySet()
                               .stream()
                               .map(item -> new Entry(item.getKey(), item.getValue()))
                               .toList());
        if(!entries.isEmpty())
        {
            result.add(new EntriesPerConnection(connection, entries));
        }
        return result;
    }
    public static List<EntriesPerConnection> getAll()
    {
        List<EntriesPerConnection> result = new ArrayList<>();
        DATA.keySet().forEach(item -> {
            result.addAll(get(item));
        });
        return result;
    }
}
