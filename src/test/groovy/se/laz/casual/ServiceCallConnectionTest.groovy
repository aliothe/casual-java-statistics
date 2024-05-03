package se.laz.casual

import spock.lang.Specification

class ServiceCallConnectionTest extends Specification
{
   def 'failed creation'()
   {
      when:
      new ServiceCallConnection(null)
      then:
      thrown(NullPointerException)
   }

   def 'ok creation'()
   {
      given:
      def connectionName = 'foo-connection'
      when:
      ServiceCallConnection connection = new ServiceCallConnection(connectionName)
      then:
      connection.connectionName() == connectionName
   }
}
