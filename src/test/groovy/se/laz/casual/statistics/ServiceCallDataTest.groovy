package se.laz.casual.statistics


import spock.lang.Specification

import java.time.Duration
import java.time.temporal.ChronoUnit

class ServiceCallDataTest extends Specification
{
   def 'failed creation'()
   {
      when:
      new ServiceCallData(callTime, pendingTime)
      then:
      thrown(NullPointerException)
      where:
      callTime                                    || pendingTime
      Duration.of(100, ChronoUnit.MICROS)         || null
      null                                        || Duration.of(100, ChronoUnit.MICROS)
   }

   def 'ok creation'()
   {
      given:
      Duration callTime = Duration.of(1000, ChronoUnit.MICROS)
      Duration pendingTime = Duration.ZERO
      when:
      ServiceCallData data = new ServiceCallData(callTime, pendingTime)
      then:
      data.callTimeInMicroseconds() == callTime
      data.pendingTimeInMicroseconds() == pendingTime
   }

   def  'builder creation'()
   {
      given:
      long start = 0
      long end = 500
      long pending = 100
      Duration callDuration = Duration.of(end, ChronoUnit.MICROS)
      Duration pendingDuration = Duration.of(pending, ChronoUnit.MICROS)
      when:
      ServiceCallData data = ServiceCallData.newBuilder()
              .withStart(start)
              .withEnd(end)
              .withPending(pending)
              .build()
      then:
      data.callTimeInMicroseconds() == callDuration
      data.pendingTimeInMicroseconds() == pendingDuration
   }

}