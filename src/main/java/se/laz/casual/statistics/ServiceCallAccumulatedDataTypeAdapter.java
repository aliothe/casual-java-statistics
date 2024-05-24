package se.laz.casual.statistics;

import com.google.gson.JsonElement;
import com.google.gson.JsonNull;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static se.laz.casual.statistics.TimeConverter.toMicroseconds;

public class ServiceCallAccumulatedDataTypeAdapter implements JsonSerializer<ServiceCallAccumulatedData>
{
    @Override
    public JsonElement serialize(ServiceCallAccumulatedData src, Type type, JsonSerializationContext context)
    {
        if (src == null)
        {
            return JsonNull.INSTANCE;
        }
        JsonObject jsonObject = new JsonObject();
        jsonObject.addProperty("numberOfServiceCalls", src.numberOfServiceCalls());
        jsonObject.addProperty("averageTime", toMicroseconds(src.averageTime()));
        jsonObject.addProperty("minTime", toMicroseconds(src.minTime()));
        jsonObject.addProperty("maxTime", toMicroseconds(src.maxTime()));
        jsonObject.addProperty("numberOfPending", src.numberOfPending());
        jsonObject.addProperty("pendingAverageTime", toMicroseconds(src.pendingAverageTime()));
        String isoLocalDateTime = DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(src.lastCall());
        jsonObject.addProperty("lastCall", isoLocalDateTime);
        System.out.println(jsonObject);
        return jsonObject;
    }
}
