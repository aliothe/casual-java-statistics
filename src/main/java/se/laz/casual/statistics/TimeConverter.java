package se.laz.casual.statistics;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeConverter
{
    private TimeConverter()
    {}
    public static Instant toInstant(long microseconds)
    {
        return Instant.EPOCH.plus(microseconds, ChronoUnit.MICROS);
    }

    public static long toMicroseconds(Instant instant)
    {
        return ChronoUnit.MICROS.between(Instant.EPOCH, instant);
    }
    public static long toMicroseconds(Duration duration)
    {
        return duration.getSeconds() * 1000_000L + duration.getNano() / 1000L;
    }
}
