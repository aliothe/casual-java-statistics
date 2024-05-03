package se.laz.casual;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public record ServiceCallAccumulatedData(long numberOfCalls, Duration averageTime, Duration minTime, Duration maxTime, long numberOfPending, Duration pendingAverageTime, LocalDateTime lastCall)
{
    public ServiceCallAccumulatedData(long numberOfCalls, Duration averageTime, Duration minTime, Duration maxTime, long numberOfPending, Duration pendingAverageTime, LocalDateTime lastCall)
    {
        Objects.requireNonNull(averageTime, "average time cannot be null");
        Objects.requireNonNull(minTime, "min time cannot be null");
        Objects.requireNonNull(maxTime, "max time cannot be null");
        Objects.requireNonNull(pendingAverageTime, "pending average time cannot be null");
        this.numberOfCalls = numberOfCalls;
        this.averageTime = averageTime;
        this.minTime = minTime;
        this.maxTime = maxTime;
        this.numberOfPending = numberOfPending;
        this.pendingAverageTime = pendingAverageTime;
        this.lastCall = lastCall;
    }

    public ServiceCallAccumulatedData accumulate(ServiceCallData serviceCallData)
    {
        LocalDateTime newLastCall = LocalDateTime.now();
        long oldMinTime = minTime.toMillis();
        long oldMaxTime = maxTime.toMillis();
        long callTime = serviceCallData.callTimeInMilliseconds().toMillis();
        Duration newAverageTime = Duration.ofMillis((averageTime.toMillis() * numberOfCalls + callTime) / (numberOfCalls + 1));
        Duration newMinTime = callTime < oldMinTime ? Duration.ofMillis(callTime) : minTime;
        Duration newMaxTime = callTime > oldMaxTime ? Duration.ofMillis(callTime) : maxTime;
        Duration newAveragePendingTime = !serviceCallData.pendingTimeInMilliseconds().isZero() ? Duration.ofMillis((pendingAverageTime.toMillis() * numberOfPending + serviceCallData.pendingTimeInMilliseconds().toMillis()) / (numberOfPending + 1)) : pendingAverageTime;
        long newNumberOfPending = !serviceCallData.pendingTimeInMilliseconds().isZero() ? numberOfPending + 1 : numberOfPending;
        return new ServiceCallAccumulatedData(numberOfCalls + 1, newAverageTime, newMinTime, newMaxTime, newNumberOfPending, newAveragePendingTime, newLastCall);
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
            long numberOfPending = serviceCallData.pendingTimeInMilliseconds().isZero() ? 0 : 1;
            return new ServiceCallAccumulatedData(1, serviceCallData.callTimeInMilliseconds(), serviceCallData.callTimeInMilliseconds(), serviceCallData.callTimeInMilliseconds(), numberOfPending, serviceCallData.pendingTimeInMilliseconds(), LocalDateTime.now());
        }
    }
}
