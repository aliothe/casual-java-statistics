package se.laz.casual.statistics.configuration;

import java.util.Objects;

public record Host(String hostName, Integer portNumber)
{
    public Host(String hostName, Integer portNumber)
    {
        Objects.requireNonNull(hostName, "hostName cannot be null");
        Objects.requireNonNull(portNumber, "portNumber cannot be null");
        this.hostName = hostName;
        this.portNumber = portNumber;
    }
}
