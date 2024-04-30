package se.laz.casual;

import java.time.LocalDateTime;
import java.util.Objects;

public class DateRange
{
    private final LocalDateTime start;
    private final LocalDateTime end;
    private DateRange(LocalDateTime start, LocalDateTime end)
    {
        this.start = start;
        this.end = end;
    }

    public LocalDateTime getStart()
    {
        return start;
    }

    public LocalDateTime getEnd()
    {
        return end;
    }

    public static DateRange of(LocalDateTime start, LocalDateTime end)
    {
        Objects.requireNonNull(start, "start cannot be null");
        Objects.requireNonNull(end, "end cannot be null");
        return new DateRange(start, end);
    }
}
