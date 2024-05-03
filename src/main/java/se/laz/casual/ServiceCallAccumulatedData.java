package se.laz.casual;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

import static se.laz.casual.TimeConverter.toDuration;
import static se.laz.casual.TimeConverter.toMicroSeconds;

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
        long oldMinTime = toMicroSeconds(minTime);
        long oldMaxTime = toMicroSeconds(maxTime);
        long callTime = toMicroSeconds(serviceCallData.callTimeInMicroseconds());
        Duration newAverageTime = toDuration((toMicroSeconds(averageTime) * numberOfCalls + callTime) / (numberOfCalls + 1));
        Duration newMinTime = callTime < oldMinTime ? toDuration(callTime) : minTime;
        Duration newMaxTime = callTime > oldMaxTime ? toDuration(callTime) : maxTime;
        Duration newAveragePendingTime = !serviceCallData.pendingTimeInMicroseconds().isZero() ? toDuration((toMicroSeconds(pendingAverageTime) * numberOfPending + toMicroSeconds(serviceCallData.pendingTimeInMicroseconds())) / (numberOfPending + 1)) : pendingAverageTime;
        long newNumberOfPending = !serviceCallData.pendingTimeInMicroseconds().isZero() ? numberOfPending + 1 : numberOfPending;
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
            long numberOfPending = serviceCallData.pendingTimeInMicroseconds().isZero() ? 0 : 1;
            return new ServiceCallAccumulatedData(1, serviceCallData.callTimeInMicroseconds(), serviceCallData.callTimeInMicroseconds(), serviceCallData.callTimeInMicroseconds(), numberOfPending, serviceCallData.pendingTimeInMicroseconds(), LocalDateTime.now());
        }
    }
}
