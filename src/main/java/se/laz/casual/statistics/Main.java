package se.laz.casual.statistics;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import se.laz.casual.statistics.configuration.Configuration;
import se.laz.casual.statistics.configuration.ConfigurationService;
import se.laz.casual.statistics.pool.ClientPool;

import java.util.UUID;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.function.BooleanSupplier;

@QuarkusMain
public class Main
{
    public static void main(String... args)
    {
        Quarkus.run(StatisticsApp.class, args);
    }
    public static class StatisticsApp implements QuarkusApplication, BooleanSupplier
    {
        private ClientPool pool;
        private EventWriter eventWriter;
        private boolean keepRunning = true;
        private ScheduledExecutorService scheduledExecutorService;

        @Override
        public int run(String... args)
        {
            UUID domainId = UUID.randomUUID();
            this.eventWriter = new EventWriter(AugmentedEventStoreFactory.getStore(domainId), ServiceCallStatistics::store, this);
            this.scheduledExecutorService = Executors.newScheduledThreadPool(4);
            Configuration configuration = ConfigurationService.getInstance().getConfiguration();
            this.pool = ClientPool.of(configuration, 30_000L, scheduledExecutorService::schedule, domainId);
            pool.connect();
            Quarkus.waitForExit();
            return 0;
        }

        @Override
        public boolean getAsBoolean()
        {
            return keepRunning;
        }
    }
}
