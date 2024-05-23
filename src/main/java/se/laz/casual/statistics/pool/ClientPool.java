package se.laz.casual.statistics.pool;

import org.eclipse.microprofile.config.inject.ConfigProperty;
import se.laz.casual.api.util.work.BackoffHelper;
import se.laz.casual.event.ServiceCallEventStore;
import se.laz.casual.event.ServiceCallEventStoreFactory;
import se.laz.casual.statistics.AugmentedEventStore;
import se.laz.casual.statistics.AugmentedEventStoreFactory;
import se.laz.casual.statistics.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class ClientPool implements ClientListener
{
    private static final UUID DOMAIN_ID = UUID.randomUUID();
    private final List<Client> clients = new ArrayList<>();
    private final Configuration configuration;
    private final ScheduleFunction scheduleFunction;
    @ConfigProperty(name = "MAX_BACKOFF_MILLISECONDS", defaultValue = "30000")
    int maxBackoffMilliseconds;
    private ClientPool(Configuration configuration, ScheduleFunction scheduleFunction)
    {
        this.configuration = configuration;
        this.scheduleFunction = scheduleFunction;
    }
    public static ClientPool of(Configuration configuration, ScheduleFunction scheduleFunction)
    {
        Objects.requireNonNull(configuration, "configuration cannot be null");
        Objects.requireNonNull(scheduleFunction, "scheduleFunction cannot be null");
        return new ClientPool(configuration, scheduleFunction);
    }
    public void connect()
    {
        configuration.hosts().parallelStream().forEach(this::connect);
    }

    private void connect(Host host)
    {
        AugmentedEventStore eventStore = AugmentedEventStoreFactory.getStore(DOMAIN_ID);
        Supplier<Client> clientSupplier = () -> {
            Client client = Client.of(host, this, eventStore);
            client.connect();
            return client;
        };
        Consumer<Client> clientConsumer = clients::add;
        new RepeatUntilSuccessTask<>(clientSupplier, clientConsumer, scheduleFunction, BackoffHelper.of(maxBackoffMilliseconds)).start();
    }
    @Override
    public void disconnected(Client client)
    {
        clients.removeIf(instance -> Objects.equals(instance, client));
        connect(client.getHost());
    }
}
