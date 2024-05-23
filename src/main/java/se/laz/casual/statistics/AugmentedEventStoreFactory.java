package se.laz.casual.statistics;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.LinkedBlockingDeque;

public record AugmentedEventStoreFactory()
{
    private static final Map<UUID, AugmentedEventStore> STORES = new ConcurrentHashMap<>();
    public static AugmentedEventStore getStore(UUID domainId)
    {
        return STORES.computeIfAbsent(domainId, id -> new AugmentedEventStore(new LinkedBlockingDeque<>()));
    }
}
