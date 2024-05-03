package se.laz.casual

import spock.lang.Specification

import java.time.LocalDateTime

class ServiceCallAccumulatedDataTest extends Specification
{
   def 'accumulate'()
   {
      given:
      def numberOfCalls = 1
      def numberOfPending = 1
      def initialCallTime = 100
      def secondCallTime = 50
      def initialPendingTime = 5
      ServiceCallData initialData = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(initialCallTime)
              .withPending(initialPendingTime)
              .build()
      ServiceCallData noPendingData = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(secondCallTime)
              .build()
      when:
      ServiceCallAccumulatedData accumulatedData = ServiceCallAccumulatedData.newBuilder()
              .withServiceCallData(initialData)
              .build()
      then:
      accumulatedData.numberOfCalls() == numberOfCalls
      accumulatedData.minTime().toMillis() == initialCallTime
      accumulatedData.maxTime().toMillis() == initialCallTime
      accumulatedData.averageTime().toMillis() == initialCallTime
      accumulatedData.numberOfPending() == numberOfPending
      accumulatedData.pendingAverageTime().toMillis() == initialPendingTime
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
      when:
      accumulatedData = accumulatedData.accumulate(noPendingData)
      then:
      accumulatedData.numberOfCalls() == ++numberOfCalls
      accumulatedData.minTime().toMillis() == secondCallTime
      accumulatedData.maxTime().toMillis() == initialCallTime
      accumulatedData.averageTime().toMillis() == (initialCallTime + secondCallTime) / numberOfCalls
      accumulatedData.numberOfPending() == numberOfPending
      accumulatedData.pendingAverageTime().toMillis() == initialPendingTime
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
   }
}
