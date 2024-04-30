package se.laz.casual;

import io.quarkus.hibernate.orm.panache.PanacheEntity;
import jakarta.persistence.Entity;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;

@Entity
public class ServiceCallEntity extends PanacheEntity
{
    public String connectionName;
    public String service;
    public String parent;
    public long pid;
    public String execution;
    public String transactionId;
    public long start;
    public long end;
    public long pending;
    public String code;
    public char order;
    @CreationTimestamp
    public LocalDateTime creationDateTime;

    protected  ServiceCallEntity()
    {}

    private ServiceCallEntity(Builder builder)
    {
        connectionName = builder.connectionName;
        service = builder.service;
        parent = builder.parent;
        pid = builder.pid;
        execution = builder.execution;
        transactionId = builder.transactionId;
        start = builder.start;
        end = builder.end;
        pending = builder.pending;
        code = builder.code;
        order = builder.order;
    }


    // operations
    public static List<ServiceCallEntity> findAllByServiceNameAndConnection(String serviceName, String connectionName)
    {
        Objects.requireNonNull(serviceName, "service name cannot be null");
        Objects.requireNonNull(connectionName, "connection name cannot be null");

        return list("service = ?1 and connectionName = ?2", serviceName, connectionName);
    }

    public static List<ServiceCallEntity> findAllByServiceName(String serviceName)
    {
        Objects.requireNonNull(serviceName, "service name cannot be null");
        return list("service = ?1", serviceName);
    }

    public static List<ServiceCallEntity> rangeByServiceNameAndConnection(DateRange range, String serviceName, String connectionName)
    {
        Objects.requireNonNull(range, "range cannot be null");
        Objects.requireNonNull(serviceName, "service name cannot be null");
        Objects.requireNonNull(connectionName, "connection name cannot be null");
        return list("serviceName = ?1 and connectionName = ?2 and timestamp between ?3 and ?4", serviceName, range.getStart(), range.getEnd());
    }

    public static List<ServiceCallEntity> rangeByServiceName(DateRange range, String serviceName)
    {
        Objects.requireNonNull(range, "range cannot be null");
        Objects.requireNonNull(serviceName, "service name cannot be null");
        return list("serviceName = ?1 and timestamp between ?2 and ?3", serviceName, range.getStart(), range.getEnd());
    }

    // builder
    public static Builder newBuilder()
    {
        return new Builder();
    }

    public static final class Builder
    {
        private String connectionName;
        private String service;
        private String parent;
        private long pid;
        private String execution;
        private String transactionId;
        private long start;
        private long end;
        private long pending;
        private String code;
        private char order;

        public Builder withConnectionName(String connectionName)
        {
            this.connectionName = connectionName;
            return this;
        }

        public Builder withService(String service)
        {
            this.service = service;
            return this;
        }

        public Builder withParent(String parent)
        {
            this.parent = parent;
            return this;
        }

        public Builder withPid(long pid)
        {
            this.pid = pid;
            return this;
        }

        public Builder withExecution(String execution)
        {
            this.execution = execution;
            return this;
        }

        public Builder withTransactionId(String transactionId)
        {
            this.transactionId = transactionId;
            return this;
        }

        public Builder withStart(long start)
        {
            this.start = start;
            return this;
        }

        public Builder withEnd(long end)
        {
            this.end = end;
            return this;
        }

        public Builder withPending(long pending)
        {
            this.pending = pending;
            return this;
        }

        public Builder withCode(String code)
        {
            this.code = code;
            return this;
        }

        public Builder withOrder(char order)
        {
            this.order = order;
            return this;
        }

        public ServiceCallEntity build()
        {
            Objects.requireNonNull(connectionName, "connectionName cannot be null");
            Objects.requireNonNull(service, "service name cannot be null");
            Objects.requireNonNull(parent, "parent cannot be null");
            Objects.requireNonNull(pid, "pid cannot be null");
            Objects.requireNonNull(execution, "execution cannot be null");
            Objects.requireNonNull(transactionId, "transactionId cannot be null");
            Objects.requireNonNull(start, "start cannot be null");
            Objects.requireNonNull(end, "end cannot be null");
            Objects.requireNonNull(pending, "pending cannot be null");
            Objects.requireNonNull(code, "code cannot be null");
            Objects.requireNonNull(order, "order cannot be null");
            return new ServiceCallEntity(this);
        }
    }
}
