package se.laz.casual.statistics.pool;

import jakarta.enterprise.inject.Produces;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;

public class ScheduledExecutorServiceProducer
{
    @Produces
    public ScheduledExecutorService get()
    {
        return Executors.newScheduledThreadPool(4);
    }
}
