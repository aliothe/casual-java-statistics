package se.laz.casual.statistics.resource;

import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import se.laz.casual.statistics.GsonProvider;
import se.laz.casual.statistics.ServiceCallStatistics;

import java.util.logging.Level;
import java.util.logging.Logger;

@Path("/statistics")
public class StatisticsResource
{
    private static final Logger LOG = Logger.getLogger(StatisticsResource.class.getName());
    @Path("all")
    @GET
    @Produces(MediaType.APPLICATION_JSON)
    public Response all()
    {
        try
        {
            String json = GsonProvider.getGson().toJson(ServiceCallStatistics.getAll());
            return Response.ok(json).build();
        }
        catch(Exception e)
        {
            LOG.log(Level.WARNING, e, () -> "Failed to get all statistics");
            return Response.serverError().entity(e.getMessage()).build();
        }
    }
}
