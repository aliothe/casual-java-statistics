package se.laz.casual;

import java.time.Duration;
import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Objects;

public record ServiceCallData(Duration callTimeInMicroseconds, Duration pendingTimeInMicroseconds)
{
    public ServiceCallData
    {
        Objects.requireNonNull(callTimeInMicroseconds, "callTimeInMilliseconds cannot be null");
        Objects.requireNonNull(pendingTimeInMicroseconds, "pendingTimeInMilliseconds cannot be null");
    }
    public static Builder newBuilder()
    {
        return new Builder();
    }
    public static final class Builder
    {
        private long start;
        private long end;
        private long pending;

        public Builder withStart(long start)
        {
            this.start = start;
            return this;
        }
        public Builder withEnd(long end)
        {
            this.end = end;
            return this;
        }
        public Builder withPending(long pending)
        {
            this.pending = pending;
            return this;
        }
        public ServiceCallData build()
        {
            Duration pendingTime = Duration.of(pending, ChronoUnit.MICROS);
            Instant startTime = TimeConverter.toInstant(start);
            Instant endTime = TimeConverter.toInstant(end);
            return new ServiceCallData(Duration.between(startTime, endTime), pendingTime);
        }

    }
}
