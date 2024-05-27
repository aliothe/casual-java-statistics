package se.laz.casual.statistics.pool;

import java.util.Objects;

public record Host(String hostName, Integer portNumber)
{
    public Host
    {
        Objects.requireNonNull(hostName, "hostName cannot be null");
        Objects.requireNonNull(portNumber, "portNumber cannot be null");
    }
    public String connectionName()
    {
        return String.format("%s:%s",hostName, portNumber);
    }
}
