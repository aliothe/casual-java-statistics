package se.laz.casual;

import java.time.Instant;
import java.util.concurrent.TimeUnit;

public class TimeConverter
{
    public static Instant toInstant(long microseconds)
    {
        long seconds = TimeUnit.MICROSECONDS.toSeconds(microseconds);
        long nanos = TimeUnit.MICROSECONDS.toNanos(microseconds % 1_000_000);
        return Instant.ofEpochSecond(seconds, nanos);
    }

    public static long toMicroSeconds(Instant instant)
    {
        long seconds = instant.getEpochSecond();
        long nanos = instant.getNano();
        return seconds * 1_000_000 + nanos / 1_000;
    }
}
