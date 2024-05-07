package se.laz.casual;

import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCallStatistics
{
    private static final Map<ServiceCallConnection, Map<ServiceCall, ServiceCallAccumulatedData>> statistics = new ConcurrentHashMap<>();
    private static final Object lock = new Object();
    public static void store(ServiceCallConnection connection, ServiceCall serviceCall, ServiceCallData data)
    {
        Objects.requireNonNull(connection, "connection can not be null");
        Objects.requireNonNull(serviceCall, "serviceCall can not be null");
        Objects.requireNonNull(data, "data can not be null");
        synchronized (lock)
        {
            Map<ServiceCall, ServiceCallAccumulatedData> accumulatedByServiceCall = statistics.computeIfAbsent(connection, k -> new ConcurrentHashMap<>());
            ServiceCallAccumulatedData maybeCachedData = accumulatedByServiceCall.get(serviceCall);
            maybeCachedData = null != maybeCachedData ? maybeCachedData.accumulate(data) : ServiceCallAccumulatedData.newBuilder()
                                                                                                                     .withServiceCallData(data).build();
            accumulatedByServiceCall.put(serviceCall, maybeCachedData);
        }
    }

    public static Optional<ServiceCallAccumulatedData> fetch(ServiceCallConnection connection, ServiceCall serviceCall)
    {
        Objects.requireNonNull(connection, "connection can not be null");
        Objects.requireNonNull(serviceCall, "serviceCall can not be null");
        Optional<Map<ServiceCall, ServiceCallAccumulatedData>> maybeAccumulatedByServiceCall = Optional.ofNullable(statistics.get(connection));
        NullableOptionalData<ServiceCallAccumulatedData> maybeAccumulatedData = NullableOptionalData.of();
        maybeAccumulatedByServiceCall.ifPresent(values -> maybeAccumulatedData.setAccumulatedData(values.get(serviceCall)));
        return maybeAccumulatedData.getAccumulatedData();
    }

    static class NullableOptionalData<T>
    {
        private T accumulatedData;
        public static NullableOptionalData of()
        {
            return new NullableOptionalData();
        }
        public void setAccumulatedData(T accumulatedData)
        {
            this.accumulatedData = accumulatedData;
        }
        public Optional<T> getAccumulatedData()
        {
            return Optional.ofNullable(accumulatedData);
        }
    }

}
