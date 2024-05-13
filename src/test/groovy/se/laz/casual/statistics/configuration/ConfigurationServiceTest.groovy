package se.laz.casual.statistics.configuration

import spock.lang.Ignore
import spock.lang.Specification

import static com.github.stefanbirkner.systemlambda.SystemLambda.withEnvironmentVariable
@Ignore
class ConfigurationServiceTest extends Specification
{
   ConfigurationService instance

   ConfigurationService reinitialiseConfigurationService()
   {
      //instance = new ConfigurationService();
   }
   def setup()
   {
      reinitialiseConfigurationService()
   }

   def 'foo'()
   {
      when:
      String actual
      withEnvironmentVariable( envVarName, envVarValue)
              .execute( {
                 actual = System.getenv(envVarName)
              })
      then:
      actual == envVarValue
      where:
      envVarName                || envVarValue
      'FOO_FOO'                 || 'ASDF'
   }

   def 'asdf'()
   {
      given:
      Configuration expected = new Configuration(hosts)
      when:
      Configuration actual
      withEnvironmentVariable( ConfigurationService.ENV_VAR_NAME, "src/test/resources/" + file )
                .execute( {
                   reinitialiseConfigurationService()
                   actual = instance.getConfiguration()
                })
      then:
      actual == expected
      where:
      file              || hosts
      'ok-config.json'  || [new Host('fast', 2134), new Host('slow', 556)]
   }
}
