package se.laz.casual;

import java.util.Objects;

public record ServiceCall(String serviceName)
{
    public ServiceCall
    {
        Objects.requireNonNull(serviceName, "serviceName cannot be null");
    }
}
