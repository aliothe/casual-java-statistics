package se.laz.casual.statistics;

@FunctionalInterface
public interface StoreFunction
{
    void store(ServiceCallConnection connection, ServiceCall serviceCall, ServiceCallData data);
}
