package se.laz.casual.statistics;

import se.laz.casual.api.CasualRuntimeException;
import java.io.Serial;

public class AugmentedEventStoreInterruptedException extends CasualRuntimeException
{
    @Serial
    private static final long serialVersionUID = 1L;
    public AugmentedEventStoreInterruptedException(String msg, Exception e)
    {
        super(msg, e);
    }
}
