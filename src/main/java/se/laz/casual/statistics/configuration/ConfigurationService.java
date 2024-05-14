package se.laz.casual.statistics.configuration;

import se.laz.casual.api.external.json.JsonProviderFactory;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

public class ConfigurationService
{
    public static final String ENV_VAR_NAME = "CASUAL_STATISTICS_CONFIGURATION_FILE";
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
        try
        {
            return JsonProviderFactory.getJsonProvider().fromJson(new FileReader(configurationFile, StandardCharsets.UTF_8), Configuration.class);
        }
        catch (FileNotFoundException e)
        {
            throw new RuntimeException(e);
        }
        catch (IOException e)
        {
            throw new RuntimeException(e);
        }
    }
}
