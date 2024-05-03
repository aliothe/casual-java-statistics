import se.laz.casual.ServiceCallData
import spock.lang.Specification

import java.time.Duration

import static se.laz.casual.TimeConverter.toDuration
import static se.laz.casual.TimeConverter.toMicroSeconds

class ServiceCallDataTest extends Specification
{
   def 'failed creation'()
   {
      when:
      new ServiceCallData(callTime, pendingTime)
      then:
      thrown(NullPointerException)
      where:
      callTime                       || pendingTime
      toDuration(100)         || null
      null                           || toDuration(100)
   }

   def 'ok creation'()
   {
      given:
      Duration callTime = toDuration(1000)
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
      when:
      ServiceCallData data = ServiceCallData.newBuilder()
              .withStart(start)
              .withEnd(end)
              .withPending(pending)
              .build()
      then:
      toMicroSeconds(data.callTimeInMicroseconds()) == (end - start)
      toMicroSeconds(data.pendingTimeInMicroseconds()) == pending
   }

}