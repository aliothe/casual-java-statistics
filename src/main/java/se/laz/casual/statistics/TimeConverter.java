package se.laz.casual.statistics;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class TimeConverter
{
    public static final double MICROSECONDS_TO_SECONDS_FACTOR = 1000_000D;
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
    public static double toSecondsWithPrecision(Duration value, int precision)
    {
        return roundUpWithPrecision(toMicroseconds(value) / MICROSECONDS_TO_SECONDS_FACTOR, precision);
    }
    public static double roundUpWithPrecision(double value, int precision)
    {
        return BigDecimal.valueOf(value).setScale(precision, RoundingMode.UP).doubleValue();
    }
}
