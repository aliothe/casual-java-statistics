package se.laz.casual

import spock.lang.Specification

import java.time.Duration
import java.time.Instant
import static se.laz.casual.TimeConverter.toInstant
import static se.laz.casual.TimeConverter.toMicroSeconds
import static se.laz.casual.TimeConverter.toDuration

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

   def 'round trip duration'()
   {
      given:
      long microseconds = 1000_000_000
      when:
      Duration duration = toDuration(microseconds)
      then:
      duration.toMillis() == microseconds / 1000
      when:
      long fromDuration = toMicroSeconds(duration)
      then:
      microseconds == fromDuration
   }
}
