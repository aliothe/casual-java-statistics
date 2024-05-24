package se.laz.casual.statistics.configuration;

import org.jboss.logmanager.Level;
import se.laz.casual.api.external.json.JsonProviderFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;
import java.util.logging.Logger;

public class ConfigurationService
{
    public static final String ENV_VAR_NAME = "CASUAL_STATISTICS_CONFIGURATION_FILE";
    private static final Logger LOG = Logger.getLogger(ConfigurationService.class.getName());
    private static final ConfigurationService INSTANCE = new ConfigurationService();
    private String configurationFile;
    private ConfigurationService()
    {
        configurationFile = Optional.ofNullable(System.getenv(ENV_VAR_NAME)).orElse(null);
    }
    public static ConfigurationService getInstance()
    {
        return INSTANCE;
    }
    public Configuration getConfiguration()
    {
        if(null == configurationFile)
        {
            throw new RuntimeException(ENV_VAR_NAME + " is not set");
        }
        return doGetConfiguration(configurationFile);
    }
    public Configuration getConfiguration(String filename)
    {
        return doGetConfiguration(filename);
    }
    private Configuration doGetConfiguration(String filename)
    {
        Objects.requireNonNull(filename, "filename cannot be null");
        try
        {
            return JsonProviderFactory.getJsonProvider().fromJson(new FileReader(filename, StandardCharsets.UTF_8), Configuration.class);
        }
        catch (FileNotFoundException e)
        {
            LOG.log(Level.WARNING, e, () -> "Failed to load configuration file: " + filename);
            throw new RuntimeException("could not find configuration file: " + filename, e);
        }
        catch (IOException e)
        {
            LOG.log(Level.WARNING, e, () -> "Failed to load configuration file: " + filename);
            throw new RuntimeException(e);
        }
    }
}
