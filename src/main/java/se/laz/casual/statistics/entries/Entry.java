package se.laz.casual.statistics.entries;

import se.laz.casual.statistics.ServiceCall;
import se.laz.casual.statistics.ServiceCallAccumulatedData;

import java.util.Objects;

public record Entry(ServiceCall serviceCall, ServiceCallAccumulatedData accumulatedData)
{
    public Entry
    {
        Objects.requireNonNull(serviceCall, "serviceCall can not be null");
        Objects.requireNonNull(accumulatedData, "accumulatedData can not be null");
    }
}
