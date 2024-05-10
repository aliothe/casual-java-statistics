package se.laz.casual

import spock.lang.Specification

import java.time.Instant

import static se.laz.casual.TimeConverter.toInstant
import static se.laz.casual.TimeConverter.toMicroSeconds

class TimeConverterTest extends Specification
{
   def 'round trip instant'()
   {
      given:
      long microseconds = 1000_000_000
      when:
      Instant instant = toInstant(microseconds)
      then:
      instant.toEpochMilli() == microseconds / 1000
      when:
      long fromInstant = toMicroSeconds(instant)
      then:
      microseconds == fromInstant
   }
}
