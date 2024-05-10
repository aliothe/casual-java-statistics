package se.laz.casual

import spock.lang.Specification

import java.time.Duration
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class ServiceCallAccumulatedDataTest extends Specification
{
   def 'accumulate'()
   {
      given:
      def numberOfCalls = 1
      def numberOfPending = 1
      def initialCallTimeMicros = 1000
      def secondCallTimeMicros = 5000
      def thirdCallTimeMicros = 12500
      def initialPendingtimeMicros = 5000
      def secondPendingtimeMicros = 1000
      Duration initialCallTime = Duration.of(initialCallTimeMicros, ChronoUnit.MICROS)
      Duration secondCallTime = Duration.of(secondCallTimeMicros, ChronoUnit.MICROS)
      Duration thirdCallTime = Duration.of(thirdCallTimeMicros, ChronoUnit.MICROS)
      Duration initialPendingTime = Duration.of(initialPendingtimeMicros, ChronoUnit.MICROS)
      Duration secondPendingTime = Duration.of(secondPendingtimeMicros, ChronoUnit.MICROS)
      ServiceCallData initialData = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(initialCallTimeMicros)
              .withPending(initialPendingtimeMicros)
              .build()
      ServiceCallData noPendingData = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(secondCallTimeMicros)
              .build()
      ServiceCallData moreDataWithPending = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(thirdCallTimeMicros)
              .withPending(secondPendingtimeMicros)
              .build()
      when:
      ServiceCallAccumulatedData accumulatedData = ServiceCallAccumulatedData.newBuilder()
              .withServiceCallData(initialData)
              .build()
      then: // initial check
      accumulatedData.numberOfServiceCalls() == numberOfCalls
      accumulatedData.minTime() == initialCallTime
      accumulatedData.maxTime() == initialCallTime
      accumulatedData.averageTime() == initialCallTime
      accumulatedData.numberOfPending() == numberOfPending
      accumulatedData.pendingAverageTime() == initialPendingTime
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
      when: // add call but no pending
      accumulatedData = accumulatedData.accumulate(noPendingData)
      then:
      accumulatedData.numberOfServiceCalls() == ++numberOfCalls
      accumulatedData.minTime() == initialCallTime
      accumulatedData.maxTime() == secondCallTime
      accumulatedData.averageTime() == initialCallTime.plus(secondCallTime).dividedBy(numberOfCalls)
      accumulatedData.numberOfPending() == numberOfPending
      accumulatedData.pendingAverageTime() == initialPendingTime
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
      when: // add call with pending
      accumulatedData = accumulatedData.accumulate(moreDataWithPending)
      then:
      accumulatedData.numberOfServiceCalls() == ++numberOfCalls
      accumulatedData.minTime() == initialCallTime
      accumulatedData.maxTime() == thirdCallTime
      accumulatedData.averageTime() == initialCallTime.plus(secondCallTime).plus(thirdCallTime).dividedBy(numberOfCalls)
      accumulatedData.numberOfPending() == ++numberOfPending
      accumulatedData.pendingAverageTime() == initialPendingTime.plus(secondPendingTime).dividedBy( numberOfPending)
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
   }
}
