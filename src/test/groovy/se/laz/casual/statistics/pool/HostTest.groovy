package se.laz.casual.statistics.pool

import spock.lang.Specification

class HostTest extends Specification
{
   def 'failed creation'()
   {
      when:
      new Host(host, port)
      then:
      thrown(NullPointerException)
      where:
      host   || port
      null   || 1234
      'asdf' || null
   }
   def 'working as expected'()
   {
      given:
      def hostName = 'shiny'
      def portNumber = 1234
      when:
      def host = new Host(hostName, portNumber)
      then:
      host.hostName() == hostName
      host.portNumber() == portNumber
      host.connectionName() == "${hostName}:${portNumber}"
   }
}
