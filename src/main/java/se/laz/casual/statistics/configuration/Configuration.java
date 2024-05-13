package se.laz.casual.statistics.configuration;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

public record Configuration(List<Host> hosts)
{
    public Configuration(List<Host> hosts)
    {
        Objects.requireNonNull(hosts, "hosts cannot be null");
        this.hosts = hosts;
    }
    public List<Host> hosts()
    {
        return Collections.unmodifiableList(hosts);
    }
}
