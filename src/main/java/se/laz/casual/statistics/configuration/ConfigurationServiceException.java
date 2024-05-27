package se.laz.casual.statistics.configuration;

import se.laz.casual.api.CasualRuntimeException;

import java.io.Serial;

public class ConfigurationServiceException extends CasualRuntimeException
{
    @Serial
    private static final long serialVersionUID = 1L;
    public ConfigurationServiceException(String s, Throwable e)
    {
        super(s, e);
    }

    public ConfigurationServiceException(String s)
    {
        super(s);
    }
}
