import se.laz.casual.ServiceCallData
import spock.lang.Specification

import java.time.Duration

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
      Duration.ofMillis(100)         || null
      null                           || Duration.ofMillis(100)
   }

   def 'ok creation'()
   {
      given:
      Duration callTime = Duration.ofMillis(100)
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
      data.callTimeInMicroseconds().toMillis() == (end - start)
      data.pendingTimeInMicroseconds().toMillis() == pending
   }

}