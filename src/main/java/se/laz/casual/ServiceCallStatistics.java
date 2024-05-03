package se.laz.casual;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ServiceCallStatistics
{
    private static final Map<ServiceCallConnection, Map<ServiceCall, ServiceCallAccumulatedData>> statistics = new ConcurrentHashMap<>();

}
