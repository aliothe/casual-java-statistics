package se.laz.casual.statistics.pool;

import jakarta.enterprise.inject.Produces;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import se.laz.casual.statistics.configuration.Configuration;
import se.laz.casual.statistics.configuration.ConfigurationService;

public class ConfigurationProducer
{
    @ConfigProperty(name="CASUAL_STATISTICS_CONFIGURATION_FILE", defaultValue = "pools.json")
    String poolFileName;
    @Produces
    public Configuration get()
    {
        return ConfigurationService.getInstance().getConfiguration(poolFileName);
    }
}
