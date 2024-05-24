package se.laz.casual.statistics.pool;

import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.eclipse.microprofile.config.inject.ConfigProperty;
import se.laz.casual.api.util.work.BackoffHelper;
import se.laz.casual.statistics.AugmentedEventStore;
import se.laz.casual.statistics.AugmentedEventStoreFactory;
import se.laz.casual.statistics.configuration.Configuration;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.function.Consumer;
import java.util.function.Supplier;
import java.util.logging.Logger;

@ApplicationScoped
public class ClientPool implements ClientListener
{
    private static final Logger LOG = Logger.getLogger(ClientPool.class.getName());
    private final List<Client> clients = new ArrayList<>();
    private UUID domainId;
    private Configuration configuration;
    private ScheduleFunction scheduleFunction;
    @ConfigProperty(name = "MAX_BACKOFF_MILLISECONDS", defaultValue = "30000")
    int maxBackoffMilliseconds;
    @Inject
    public ClientPool(Configuration config)
    {
        this.configuration = config;
    }
    public void initialize(ScheduleFunction scheduleFunction, UUID domainId)
    {
        Objects.requireNonNull(scheduleFunction, "scheduleFunction cannot be null");
        Objects.requireNonNull(domainId, "domainId cannot be null");
        this.scheduleFunction = scheduleFunction;
        this.domainId = domainId;
    }
    public void connect()
    {
        configuration.hosts().parallelStream().forEach(this::connect);
    }

    private void connect(Host host)
    {
        Objects.requireNonNull(configuration, "configuration cannot be null");
        Objects.requireNonNull(scheduleFunction, "scheduleFunction cannot be null");
        AugmentedEventStore eventStore = AugmentedEventStoreFactory.getStore(domainId);
        Supplier<Client> clientSupplier = () -> {
            Client client = Client.of(host, this, eventStore);
            client.connect();
            LOG.finest("Connected to " + host);
            return client;
        };
        Consumer<Client> clientConsumer = clients::add;
        new RepeatUntilSuccessTask<>(clientSupplier, clientConsumer, scheduleFunction, BackoffHelper.of(maxBackoffMilliseconds)).start();
    }
    @Override
    public void disconnected(Client client)
    {
        LOG.finest("Disconnected from " + client);
        clients.removeIf(instance -> Objects.equals(instance, client));
        connect(client.getHost());
    }
}
