package se.laz.casual.statistics


import spock.lang.Specification

class ServiceCallTest extends Specification
{
   def 'failed creation'()
   {
      when:
      new ServiceCall(null)
      then:
      thrown(NullPointerException)
   }

   def 'ok creation'()
   {
      given:
      def serviceName = 'foo-service'
      when:
      ServiceCall serviceCall = new ServiceCall(serviceName)
      then:
      serviceCall.serviceName() == serviceName
   }
}
