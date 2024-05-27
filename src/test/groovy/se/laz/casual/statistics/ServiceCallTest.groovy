package se.laz.casual.statistics

import se.laz.casual.event.Order
import spock.lang.Specification

class ServiceCallTest extends Specification
{
   def 'failed creation'()
   {
      when:
      new ServiceCall(name, order)
      then:
      thrown(NullPointerException)
      where:
      name   || order
      null   || Order.SEQUENTIAL
      'asdf' || null
   }

   def 'ok creation'()
   {
      given:
      def serviceName = 'foo-service'
      when:
      ServiceCall serviceCall = new ServiceCall(serviceName, Order.SEQUENTIAL)
      then:
      serviceCall.serviceName() == serviceName
      serviceCall.order() == Order.SEQUENTIAL
   }
}
