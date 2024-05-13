package se.laz.casual;

@FunctionalInterface
public interface StoreFunction
{
    void store(ServiceCallConnection connection, ServiceCall serviceCall, ServiceCallData data);
}
