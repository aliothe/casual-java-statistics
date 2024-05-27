package se.laz.casual.statistics.pool;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface ScheduleFunction
{
    // sonar
    // This is the exact signature of the JDKs:
    // ScheduledExecutorService::schedule
    @SuppressWarnings("java:S1452")
    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);
}
