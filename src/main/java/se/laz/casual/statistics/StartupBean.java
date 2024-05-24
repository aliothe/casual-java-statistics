package se.laz.casual.statistics;

import io.quarkus.runtime.Startup;
import jakarta.annotation.PostConstruct;
import jakarta.enterprise.context.ApplicationScoped;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import se.laz.casual.statistics.configuration.Configuration;
import se.laz.casual.statistics.configuration.ConfigurationService;
import se.laz.casual.statistics.pool.ClientPool;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BooleanSupplier;

@Startup
@ApplicationScoped
public class StartupBean implements BooleanSupplier
{
    private ClientPool pool;
    private EventWriter eventWriter;
    private boolean keepRunning = true;
    ScheduledExecutorService scheduledExecutorService;
    @ConfigProperty(name = "max.backoff.milliseconds", defaultValue = "30000")
    int maxBackoffMilliseconds;
    @ConfigProperty(name = "casual.statistics.configuration.file", defaultValue = "pool.json")
    String configurationFileName;
    @Override
    public boolean getAsBoolean()
    {
        return keepRunning;
    }
    @PostConstruct
    public void init()
    {
        UUID domainId = UUID.randomUUID();
        this.eventWriter = new EventWriter(AugmentedEventStoreFactory.getStore(domainId), ServiceCallStatistics::store, this);
        this.scheduledExecutorService = Executors.newScheduledThreadPool(4);
        Configuration configuration = ConfigurationService.getInstance().getConfiguration(configurationFileName);
        this.pool = ClientPool.of(configuration, maxBackoffMilliseconds, scheduledExecutorService::schedule, domainId);
        pool.connect();
    }
}
