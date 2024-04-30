package se.laz.casual;

import io.quarkus.hibernate.orm.panache.PanacheRepository;

import java.util.List;

public class ServiceCallRepository implements PanacheRepository<ServiceCallEntity>
{
    public List<ServiceCallEntity> findServiceCallByServiceNameAndConnection(String serviceName, String connectionName)
    {
        return find("connectionName", connectionName, "service", serviceName).list();
    }

    public List<ServiceCallEntity> findAllServices(String serviceName)
    {
        return listAll().stream()
                        .filter(entity -> entity.service.equals(serviceName))
                        .toList();
    }

}
