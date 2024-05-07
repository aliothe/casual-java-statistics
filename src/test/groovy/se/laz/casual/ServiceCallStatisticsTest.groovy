package se.laz.casual

import spock.lang.Shared
import spock.lang.Specification

class ServiceCallStatisticsTest extends Specification
{
   @Shared
   ServiceCallConnection realServiceCallConnection = new ServiceCallConnection('a very fast connection, also blue')
   @Shared
   ServiceCall realServiceCall = new ServiceCall('a really nice service')
   @Shared
   ServiceCallData realServiceCallData = ServiceCallData.newBuilder()
                                                        .withPending(42)
                                                        .withStart(0)
                                                        .withEnd(100)
                                                        .build()
   def 'store, wrong usage'()
   {
      when:
      ServiceCallStatistics.store(connection, serviceCall, data)
      then:
      thrown(NullPointerException)
      where:
      connection                      || serviceCall            || data
      null                            || realServiceCall        || realServiceCallData
      realServiceCallConnection       || null                   || realServiceCallData
      realServiceCallConnection       || realServiceCall        || null
   }

   def 'fetch, wrong usage'()
   {
      when:
      ServiceCallStatistics.fetch(connection, serviceCall)
      then:
      thrown(NullPointerException)
      where:
      connection                      || serviceCall
      null                            || realServiceCall
      realServiceCallConnection       || null
   }
}
