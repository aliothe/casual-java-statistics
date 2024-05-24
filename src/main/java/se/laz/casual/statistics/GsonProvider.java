package se.laz.casual.statistics;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

public class GsonProvider
{
    private static final Gson GSON;
    static
    {
        GsonBuilder builder = new GsonBuilder();
        builder.registerTypeAdapter(ServiceCallAccumulatedData.class, new ServiceCallAccumulatedDataTypeAdapter());
        // enableComplexMapKeySerialization()
        //        .setPrettyPrinting()
        GSON = builder.enableComplexMapKeySerialization().setPrettyPrinting().create();
    }
    private GsonProvider()
    {}
    public static Gson getGson()
    {
        return GSON;
    }
}
