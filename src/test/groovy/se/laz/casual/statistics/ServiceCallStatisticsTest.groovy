package se.laz.casual.statistics

import se.laz.casual.api.CasualRuntimeException
import spock.lang.Shared
import spock.lang.Specification

import java.time.Duration
import java.time.temporal.ChronoUnit

class ServiceCallStatisticsTest extends Specification {
   @Shared
   ServiceCallConnection sharedServiceCallConnection = new ServiceCallConnection('a very fast connection, also blue')
   @Shared
   ServiceCall sharedServiceCall = new ServiceCall('a really nice service')
   @Shared
   ServiceCallData sharedServiceCallData = ServiceCallData.newBuilder()
           .withPending(42)
           .withStart(0)
           .withEnd(100)
           .build()

   def 'store, wrong usage'() {
      when:
      ServiceCallStatistics.store(connection, serviceCall, data)
      then:
      thrown(NullPointerException)
      where:
      connection                || serviceCall     || data
      null                      || sharedServiceCall   || sharedServiceCallData
      sharedServiceCallConnection || null            || sharedServiceCallData
      sharedServiceCallConnection || sharedServiceCall || null
   }

   def 'fetch, wrong usage'() {
      when:
      ServiceCallStatistics.fetch(connection, serviceCall)
      then:
      thrown(NullPointerException)
      where:
      connection                || serviceCall
      null                      || sharedServiceCall
      sharedServiceCallConnection || null
   }

   def 'store, fetch, accumulate, fetch'()
   {
      given:
      def start = 0
      def end = 100_000
      def secondEnd = 10_000
      def thirdEnd = end * 2
      def initialPending = 10_000
      def secondPending = 5_000
      Duration initialDuration = Duration.of(end, ChronoUnit.MICROS)
      Duration initialPendingDuration = Duration.of(initialPending, ChronoUnit.MICROS)
      Duration secondPendingDuration = Duration.of(secondPending, ChronoUnit.MICROS)
      Duration secondDuration = Duration.of(secondEnd, ChronoUnit.MICROS)
      Duration thirdDuration = Duration.of(thirdEnd, ChronoUnit.MICROS)
      ServiceCallData initialData = ServiceCallData.newBuilder()
              .withPending(0)
              .withStart(start)
              .withEnd(end)
              .build()
      ServiceCallData secondCallData = ServiceCallData.newBuilder()
              .withPending(initialPending)
              .withStart(start)
              .withEnd(secondEnd)
              .build()
      ServiceCallData thirdCallData = ServiceCallData.newBuilder()
              .withPending(secondPending)
              .withStart(start)
              .withEnd(thirdEnd)
              .build()

      when:
      ServiceCallStatistics.store(sharedServiceCallConnection, sharedServiceCall, initialData)
      ServiceCallAccumulatedData accumulatedData = ServiceCallStatistics.fetch(sharedServiceCallConnection, sharedServiceCall).orElseThrow {new CasualRuntimeException("missing entry")}
      then:
      accumulatedData.numberOfPending() == 0
      accumulatedData.pendingAverageTime() == Duration.ZERO
      accumulatedData.numberOfServiceCalls() == 1
      accumulatedData.minTime() == initialDuration
      accumulatedData.maxTime() == initialDuration
      accumulatedData.averageTime() == initialDuration
      when:
      ServiceCallStatistics.store(sharedServiceCallConnection, sharedServiceCall, secondCallData)
      accumulatedData = ServiceCallStatistics.fetch(sharedServiceCallConnection, sharedServiceCall).orElseThrow {new CasualRuntimeException("missing entry")}
      then:
      accumulatedData.numberOfPending() == 1
      accumulatedData.pendingAverageTime() == initialPendingDuration
      accumulatedData.numberOfServiceCalls() == 2
      accumulatedData.minTime() == secondDuration
      accumulatedData.maxTime() == initialDuration
      accumulatedData.averageTime() == initialDuration.plus(secondDuration).dividedBy(2)
      when:
      ServiceCallStatistics.store(sharedServiceCallConnection, sharedServiceCall, thirdCallData)
      accumulatedData = ServiceCallStatistics.fetch(sharedServiceCallConnection, sharedServiceCall).orElseThrow {new CasualRuntimeException("missing entry")}
      then:
      accumulatedData.numberOfPending() == 2
      accumulatedData.pendingAverageTime() == initialPendingDuration.plus(secondPendingDuration).dividedBy(2)
      accumulatedData.numberOfServiceCalls() == 3
      accumulatedData.minTime() == secondDuration
      accumulatedData.maxTime() == thirdDuration
      accumulatedData.averageTime() == initialDuration.plus(secondDuration).plus(thirdDuration).dividedBy(3)
   }


}
