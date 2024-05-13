package se.laz.casual.statistics;

import java.util.Objects;

public record ServiceCallConnection(String connectionName)
{
    public ServiceCallConnection
    {
        Objects.requireNonNull(connectionName, "connectionName cannot be null");
    }
}
