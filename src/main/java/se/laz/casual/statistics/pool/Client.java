/*
 * Copyright (c) 2024, The casual project. All rights reserved.
 *
 * This software is licensed under the MIT license, https://opensource.org/licenses/MIT
 */

package se.laz.casual.statistics.pool;

import se.laz.casual.event.Order;
import se.laz.casual.event.ServiceCallEvent;
import se.laz.casual.event.client.ConnectionObserver;
import se.laz.casual.event.client.EventClient;
import se.laz.casual.event.client.EventClientBuilder;
import se.laz.casual.event.client.EventObserver;
import se.laz.casual.statistics.AugmentedEvent;
import se.laz.casual.statistics.AugmentedEventStore;
import se.laz.casual.statistics.ServiceCall;
import se.laz.casual.statistics.ServiceCallConnection;
import se.laz.casual.statistics.ServiceCallData;

import java.util.Objects;

public class Client implements EventObserver, ConnectionObserver
{
    private final Host host;
    private final ClientListener clientListener;
    private final AugmentedEventStore eventStore;

    public Client(Host host, ClientListener clientListener, AugmentedEventStore eventStore)
    {
        this.host = host;
        this.clientListener = clientListener;
        this.eventStore = eventStore;
    }
    public static Client of(Host host, ClientListener clientListener, AugmentedEventStore eventStore)
    {
        Objects.requireNonNull(host, "host can not be null");
        Objects.requireNonNull(clientListener, "clientListener can not be null");
        Objects.requireNonNull(eventStore, "eventStore can not be null");
        return new Client(host, clientListener, eventStore);
    }
    public void connect()
    {
        EventClient client = EventClientBuilder.createBuilder()
                                               .withHost(host.hostName())
                                               .withPort(host.portNumber())
                                               .withEventLoopGroup(EventLoopGroupFactory.getInstance())
                                               .withConnectionObserver(this)
                                               .withEventObserver(this)
                                               .build();
        client.connect().join();
    }
    public Host getHost()
    {
        return host;
    }
    @Override
    public void notify(ServiceCallEvent event)
    {
        ServiceCallConnection connection = new ServiceCallConnection(host.connectionName());
        ServiceCall serviceCall = new ServiceCall(event.getService(), Order.unmarshall(event.getOrder()));
        ServiceCallData data = ServiceCallData.newBuilder()
                                              .withStart(event.getStart())
                                              .withEnd(event.getEnd())
                                              .withPending(event.getPending())
                                              .build();
        eventStore.put(new AugmentedEvent(connection, serviceCall, data));
    }
    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (!(o instanceof Client client))
        {
            return false;
        }
        return Objects.equals(host, client.host) && Objects.equals(clientListener, client.clientListener);
    }
    @Override
    public int hashCode()
    {
        return Objects.hash(host, clientListener);
    }
    @Override
    public String toString()
    {
        return "Client{" +
                "host=" + host +
                ", connectionObserver=" + clientListener +
                '}';
    }
    @Override
    public void disconnected(EventClient client)
    {
        clientListener.disconnected(this);
    }
}
