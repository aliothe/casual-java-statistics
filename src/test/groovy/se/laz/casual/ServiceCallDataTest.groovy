import se.laz.casual.ServiceCallData
import spock.lang.Specification

import java.time.Duration

class ServiceCallDataTest extends Specification
{
   def 'creation'()
   {
      given:
      Duration callTime = Duration.ofMillis(100)
      Duration pendingTime = Duration.ZERO
      when:
      ServiceCallData data = new ServiceCallData(callTime, pendingTime)
      then:
      data.callTimeInMilliseconds() == callTime
      data.pendingTimeInMilliseconds() == pendingTime
   }
}