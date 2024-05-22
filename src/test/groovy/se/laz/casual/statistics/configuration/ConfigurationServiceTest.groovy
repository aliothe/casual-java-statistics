package se.laz.casual.statistics.configuration

import se.laz.casual.statistics.pool.Host
import spock.lang.Specification

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable

class ConfigurationServiceTest extends Specification
{
   def 'host configuration from file'()
   {
      given:
      Configuration expected = new Configuration(hosts)
      ConfigurationService instance
      when:
      Configuration actual
      withEnvironmentVariable( ConfigurationService.ENV_VAR_NAME, "src/test/resources/" + file )
                .execute( {
                   instance = new ConfigurationService()
                   actual = instance.getConfiguration()
                })
      then:
      actual == expected
      where:
      file                || hosts
      'host-config.json'  || [new Host('fast', 2134), new Host('slow', 556)]
   }
}
