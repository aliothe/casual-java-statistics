package se.laz.casual.statistics;

import se.laz.casual.event.ServiceCallEventStore;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCallStatistics
{
    private static final Map<ServiceCallConnection, Map<ServiceCall, ServiceCallAccumulatedData>> statistics = new ConcurrentHashMap<>();
    public static void store(ServiceCallConnection connection, ServiceCall serviceCall, ServiceCallData data)
    {
        Objects.requireNonNull(connection, "connection can not be null");
        Objects.requireNonNull(serviceCall, "serviceCall can not be null");
        Objects.requireNonNull(data, "data can not be null");
        // note, compute is atomic
        statistics.compute(connection, (conn, accumulatedByServiceCall) -> {
            if (accumulatedByServiceCall == null)
            {
                accumulatedByServiceCall = new ConcurrentHashMap<>();
            }
            accumulatedByServiceCall.compute(serviceCall, (call, maybeCachedData) -> null == maybeCachedData ?  ServiceCallAccumulatedData.newBuilder().withServiceCallData(data).build() : maybeCachedData.accumulate(data));
            return accumulatedByServiceCall;
        });
    }

    public static Optional<ServiceCallAccumulatedData> fetch(ServiceCallConnection connection, ServiceCall serviceCall)
    {

        Objects.requireNonNull(connection, "connection can not be null");
        Objects.requireNonNull(serviceCall, "serviceCall can not be null");
        Map<ServiceCall, ServiceCallAccumulatedData> accumulatedDataByServiceCall = statistics.get(connection);
        if(null == accumulatedDataByServiceCall)
        {
            return Optional.empty();
        }
        return Optional.ofNullable(accumulatedDataByServiceCall.get(serviceCall));
    }
}
