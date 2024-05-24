package se.laz.casual.statistics;

import io.quarkus.runtime.Quarkus;
import io.quarkus.runtime.QuarkusApplication;
import io.quarkus.runtime.annotations.QuarkusMain;
import jakarta.annotation.Resource;
import jakarta.inject.Inject;
import se.laz.casual.statistics.pool.ClientPool;

import java.util.UUID;
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
        ClientPool pool;
        private EventWriter eventWriter;
        private boolean keepRunning = true;
        @Resource
        ScheduledExecutorService scheduledExecutorService;

        @Inject
        public StatisticsApp(ClientPool pool)
        {
            this.pool = pool;
        }

        @Override
        public int run(String... args) throws Exception
        {
            UUID domainId = UUID.randomUUID();
            this.eventWriter = new EventWriter(AugmentedEventStoreFactory.getStore(domainId), ServiceCallStatistics::store, this);
            pool.initialize(scheduledExecutorService::schedule, domainId);
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
