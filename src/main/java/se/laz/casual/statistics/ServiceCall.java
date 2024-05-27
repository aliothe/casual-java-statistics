package se.laz.casual.statistics;

import se.laz.casual.event.Order;

import java.util.Objects;

public record ServiceCall(String serviceName, Order order)
{
    public ServiceCall
    {
        Objects.requireNonNull(serviceName, "serviceName cannot be null");
        Objects.requireNonNull(order, "order cannot be null");
    }
}
