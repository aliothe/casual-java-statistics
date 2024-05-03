package se.laz.casual

import spock.lang.Specification

import java.time.LocalDateTime

import static TimeConverter.toMicroSeconds;

class ServiceCallAccumulatedDataTest extends Specification
{
   def 'accumulate'()
   {
      given:
      def numberOfCalls = 1
      def numberOfPending = 1
      def initialCallTime = 1000
      def secondCallTime = 5000
      def thirdCallTime = 12500
      def initialPendingTime = 5000
      def secondPendingTime = 1000
      ServiceCallData initialData = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(initialCallTime)
              .withPending(initialPendingTime)
              .build()
      ServiceCallData noPendingData = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(secondCallTime)
              .build()
      ServiceCallData moreDataWithPending = ServiceCallData.newBuilder()
              .withStart(0)
              .withEnd(thirdCallTime)
              .withPending(secondPendingTime)
              .build()
      when:
      ServiceCallAccumulatedData accumulatedData = ServiceCallAccumulatedData.newBuilder()
              .withServiceCallData(initialData)
              .build()
      then: // initial check
      accumulatedData.numberOfCalls() == numberOfCalls
      toMicroSeconds(accumulatedData.minTime()) == initialCallTime
      toMicroSeconds(accumulatedData.maxTime()) == initialCallTime
      toMicroSeconds(accumulatedData.averageTime()) == initialCallTime
      accumulatedData.numberOfPending() == numberOfPending
      toMicroSeconds(accumulatedData.pendingAverageTime()) == initialPendingTime
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
      when: // add call but no pending
      accumulatedData = accumulatedData.accumulate(noPendingData)
      then:
      accumulatedData.numberOfCalls() == ++numberOfCalls
      toMicroSeconds(accumulatedData.minTime()) == initialCallTime
      toMicroSeconds(accumulatedData.maxTime()) == secondCallTime
      toMicroSeconds(accumulatedData.averageTime()) == (initialCallTime + secondCallTime) / numberOfCalls
      accumulatedData.numberOfPending() == numberOfPending
      toMicroSeconds(accumulatedData.pendingAverageTime()) == initialPendingTime
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
      when: // add call with pending
      accumulatedData = accumulatedData.accumulate(moreDataWithPending)
      then:
      accumulatedData.numberOfCalls() == ++numberOfCalls
      toMicroSeconds(accumulatedData.minTime()) == initialCallTime
      toMicroSeconds(accumulatedData.maxTime()) == thirdCallTime
      toMicroSeconds(accumulatedData.averageTime()) == (long)((initialCallTime + secondCallTime + thirdCallTime) / numberOfCalls)
      accumulatedData.numberOfPending() == ++numberOfPending
      toMicroSeconds(accumulatedData.pendingAverageTime()) == (initialPendingTime + secondPendingTime) / numberOfPending
      accumulatedData.lastCall().isBefore(LocalDateTime.now())
   }
}
