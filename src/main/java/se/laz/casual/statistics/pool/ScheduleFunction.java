package se.laz.casual.statistics.pool;

import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.TimeUnit;

public interface ScheduleFunction
{
    ScheduledFuture<?> schedule(Runnable command, long delay, TimeUnit unit);
}
