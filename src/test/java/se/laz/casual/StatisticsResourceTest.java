package se.laz.casual;

import io.quarkus.test.junit.QuarkusTest;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;
import se.laz.casual.api.util.time.InstantUtil;
import se.laz.casual.statistics.ServiceCall;
import se.laz.casual.statistics.ServiceCallConnection;
import se.laz.casual.statistics.ServiceCallData;
import se.laz.casual.statistics.ServiceCallStatistics;

import java.time.LocalDateTime;
import java.time.ZoneOffset;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.everyItem;
import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.Matchers.is;

@QuarkusTest
class StatisticsResourceTest
{
    @Test
    void testAllWithNoData()
    {
        given()
                .when().get("/statistics")
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
                .when().get("/statistics")
                .then()
                .statusCode(200)
                .contentType(ContentType.JSON)
                .body("size()", is(2))
                .body("connection.connectionName", hasItems("asdf", "bazinga"))
                .body("entries.flatten {it.serviceCall}.serviceName", hasItems("fast-service", "slow-service"))
                .body("entries.flatten{it.accumulatedData}.numberOfServiceCalls", everyItem(is(1)))
                .body("entries.flatten{it.accumulatedData}.averageTime", everyItem(is(8500)))
                .body("entries.flatten{it.accumulatedData}.minTime", everyItem(is(8500)))
                .body("entries.flatten{it.accumulatedData}.maxTime", everyItem(is(8500)))
                .body("entries.flatten{it.accumulatedData}.numberOfPending", everyItem(is(1)))
                .body("entries.flatten{it.accumulatedData}.pendingAverageTime", everyItem(is(2750)));
    }

}