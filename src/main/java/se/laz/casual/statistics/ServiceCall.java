package se.laz.casual.statistics;

import java.util.Objects;

public record ServiceCall(String serviceName)
{
    public ServiceCall
    {
        Objects.requireNonNull(serviceName, "serviceName cannot be null");
    }
}
