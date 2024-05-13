package se.laz.casual;

import se.laz.casual.event.ServiceCallEvent;
import se.laz.casual.event.client.EventObserver;

import java.util.Objects;

public record ServiceCallEventHandler(ServiceCallConnection connection, StoreFunction storeFunction) implements EventObserver
{
    public ServiceCallEventHandler(ServiceCallConnection connection, StoreFunction storeFunction)
    {
        Objects.requireNonNull(connection, "connection cannot be null");
        Objects.requireNonNull(storeFunction, "storeFunction cannot be null");
        this.connection = connection;
        this.storeFunction = storeFunction;
    }

    @Override
    public void notify(ServiceCallEvent serviceCallEvent)
    {
        storeFunction.store(connection, new ServiceCall(serviceCallEvent.getService()), ServiceCallData.newBuilder()
                                                                                                       .withStart(serviceCallEvent.getStart())
                                                                                                       .withEnd(serviceCallEvent.getEnd())
                                                                                                       .withPending(serviceCallEvent.getPending())
                                                                                                       .build());

    }
}
