package se.laz.casual.statistics;

import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeConverter
{
    public static Instant toInstant(long microseconds)
    {
        return Instant.EPOCH.plus(microseconds, ChronoUnit.MICROS);
    }

    public static long toMicroSeconds(Instant instant)
    {
        return ChronoUnit.MICROS.between(Instant.EPOCH, instant);
    }
}
