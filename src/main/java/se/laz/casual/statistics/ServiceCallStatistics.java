package se.laz.casual.statistics;

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
        Optional<Map<ServiceCall, ServiceCallAccumulatedData>> maybeAccumulatedByServiceCall = Optional.ofNullable(statistics.get(connection));
        NullableOptionalData<ServiceCallAccumulatedData> maybeAccumulatedData = NullableOptionalData.of();
        maybeAccumulatedByServiceCall.ifPresent(values -> maybeAccumulatedData.setAccumulatedData(values.get(serviceCall)));
        return maybeAccumulatedData.getAccumulatedData();
    }

    static class NullableOptionalData<T>
    {
        private T accumulatedData;
        public static <T>NullableOptionalData<T> of()
        {
            return new NullableOptionalData<>();
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
