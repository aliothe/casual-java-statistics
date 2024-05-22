package se.laz.casual.statistics.pool;

import se.laz.casual.event.ServiceCallEvent;
import se.laz.casual.event.client.ConnectionObserver;
import se.laz.casual.event.client.EventClient;
import se.laz.casual.event.client.EventClientBuilder;
import se.laz.casual.event.client.EventObserver;

import java.util.Objects;

public class Client implements EventObserver, ConnectionObserver
{
    private final Host host;
    private final ClientListener clientListener;
    public Client(Host host, ClientListener clientListener)
    {
        this.host = host;
        this.clientListener = clientListener;
    }
    public static Client of(Host host, ClientListener clientListener)
    {
        Objects.requireNonNull(host, "host can not be null");
        Objects.requireNonNull(clientListener, "clientListener can not be null");
        return new Client(host, clientListener);
    }
    public void connect()
    {
        EventClient client = EventClientBuilder.createBuilder()
                                               .withHost(host.hostName())
                                               .withPort(host.portNumber())
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
    public void notify(ServiceCallEvent serviceCallEvent)
    {

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
