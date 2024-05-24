package se.laz.casual;

import io.quarkus.test.junit.QuarkusTest;
import org.junit.jupiter.api.Test;
import se.laz.casual.api.util.time.InstantUtil;
import se.laz.casual.statistics.ServiceCall;
import se.laz.casual.statistics.ServiceCallConnection;
import se.laz.casual.statistics.ServiceCallData;
import se.laz.casual.statistics.ServiceCallStatistics;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.contains;

@QuarkusTest
class StatisticsResourceTest
{
    @Test
    void testAllWithNoData()
    {
        given()
                .when().get("/statistics/all")
                .then()
                .statusCode(200)
                .contentType("application/json");
    }
    @Test
    void testAllWithData()
    {
        long callTimeMicroseconds = 8500;
        long pendingTimeMicroseconds = 2750;
        LocalDateTime start = LocalDateTime.now();
        LocalDateTime end = start.plusNanos(callTimeMicroseconds * 1000);
        ServiceCallConnection connection = new ServiceCallConnection("asdf");
        ServiceCallConnection connectionTwo = new ServiceCallConnection("bazinga");
        ServiceCall serviceCall = new ServiceCall("fast-service");
        ServiceCallData data = ServiceCallData.newBuilder()
                                              .withStart(InstantUtil.toEpochMicro(start.toInstant(ZoneOffset.UTC)))
                                              .withEnd(InstantUtil.toEpochMicro(end.toInstant(ZoneOffset.UTC)))
                                              .withPending(pendingTimeMicroseconds)
                                              .build();
        ServiceCall serviceCallTwo = new ServiceCall("slow-service");
        ServiceCallStatistics.store(connection, serviceCall, data);
        ServiceCallStatistics.store(connection, serviceCallTwo, data);
        ServiceCallStatistics.store(connectionTwo, serviceCall, data);
        given()
                .when().get("/statistics/all")
                .then()
                .statusCode(200)
                .contentType("application/json")
                .body(contains("fast-service"));
    }

}