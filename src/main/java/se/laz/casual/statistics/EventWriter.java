package se.laz.casual.statistics;

import se.laz.casual.event.ServiceCallEventStore;

import java.util.Objects;

public record EventWriter(ServiceCallEventStore serviceCallEventStore, StoreFunction storeFunction)
{
    public EventWriter
    {
        Objects.requireNonNull(serviceCallEventStore, "serviceCallEventStore can not be null");
        Objects.requireNonNull(storeFunction, "storeFunction can not be null");
    }
}
