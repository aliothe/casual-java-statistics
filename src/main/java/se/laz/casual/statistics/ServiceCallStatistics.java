package se.laz.casual.statistics;

import se.laz.casual.statistics.entries.EntriesPerConnection;

import java.util.List;
import java.util.Objects;
import java.util.Optional;

public class ServiceCallStatistics
{
    private static final ServiceCallStatisticsStore STORE = new ServiceCallStatisticsStore();
    public static void store(ServiceCallConnection connection, ServiceCall serviceCall, ServiceCallData data)
    {
        Objects.requireNonNull(connection, "connection can not be null");
        Objects.requireNonNull(serviceCall, "serviceCall can not be null");
        Objects.requireNonNull(data, "data can not be null");
        STORE.store(connection, serviceCall, data);
    }
    public static Optional<ServiceCallAccumulatedData> fetch(ServiceCallConnection connection, ServiceCall serviceCall)
    {

        Objects.requireNonNull(connection, "connection can not be null");
        Objects.requireNonNull(serviceCall, "serviceCall can not be null");
        return STORE.get(connection, serviceCall);
    }
    public static List<EntriesPerConnection> get(ServiceCallConnection connection)
    {
        Objects.requireNonNull(connection, "connection can not be null");
        return STORE.get(connection);
    }
    public static List<EntriesPerConnection> getAll()
    {
        return STORE.getAll();
    }
}
