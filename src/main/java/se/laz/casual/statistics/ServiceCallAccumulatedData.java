package se.laz.casual.statistics;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public record ServiceCallAccumulatedData(long numberOfServiceCalls, Duration averageTime, Duration minTime, Duration maxTime, long numberOfPending, Duration pendingAverageTime, LocalDateTime lastCall)
{
    public ServiceCallAccumulatedData
    {
        Objects.requireNonNull(averageTime, "average time cannot be null");
        Objects.requireNonNull(minTime, "min time cannot be null");
        Objects.requireNonNull(maxTime, "max time cannot be null");
        Objects.requireNonNull(pendingAverageTime, "pending average time cannot be null");
        Objects.requireNonNull(lastCall, "last call cannot be null");
    }

    public ServiceCallAccumulatedData accumulate(ServiceCallData serviceCallData)
    {
        Duration newAverageTime = (averageTime.multipliedBy(numberOfServiceCalls).plus(serviceCallData.callTimeInMicroseconds())).dividedBy(numberOfServiceCalls + 1);
        Duration newMinTime = serviceCallData.callTimeInMicroseconds().compareTo(minTime) < 0 ? serviceCallData.callTimeInMicroseconds() : minTime;
        Duration newMaxTime = serviceCallData.callTimeInMicroseconds().compareTo(minTime) >= 0 ? serviceCallData.callTimeInMicroseconds() : maxTime;
        Duration newAveragePendingTime = !serviceCallData.pendingTimeInMicroseconds().isZero() ? accumulatePendingAverageTime(serviceCallData.pendingTimeInMicroseconds()) : pendingAverageTime;
        long newNumberOfPending = !serviceCallData.pendingTimeInMicroseconds().isZero() ? numberOfPending + 1 : numberOfPending;
        return new ServiceCallAccumulatedData(numberOfServiceCalls + 1, newAverageTime, newMinTime, newMaxTime, newNumberOfPending, newAveragePendingTime, serviceCallData.lastCall());
    }

    private Duration accumulatePendingAverageTime(Duration newPending)
    {
        return (pendingAverageTime.multipliedBy(numberOfPending).plus(newPending)).dividedBy(numberOfPending + 1);
    }

    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private ServiceCallData serviceCallData;
        public Builder withServiceCallData(ServiceCallData serviceCallData)
        {
            Objects.requireNonNull(serviceCallData, "serviceCallData cannot be null");
            this.serviceCallData = serviceCallData;
            return this;
        }
        public ServiceCallAccumulatedData build()
        {
            long numberOfPending = serviceCallData.pendingTimeInMicroseconds().isZero() ? 0 : 1;
            return new ServiceCallAccumulatedData(1, serviceCallData.callTimeInMicroseconds(), serviceCallData.callTimeInMicroseconds(), serviceCallData.callTimeInMicroseconds(), numberOfPending, serviceCallData.pendingTimeInMicroseconds(), serviceCallData.lastCall());
        }
    }
}
