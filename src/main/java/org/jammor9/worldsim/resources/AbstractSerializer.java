package org.jammor9.worldsim.resources;

import com.google.gson.*;

import java.lang.reflect.Type;


//Handles JSON saving for the Abstract resource classes
public class AbstractSerializer implements JsonSerializer<Resource>, JsonDeserializer<Resource> {
    private static final String CLASS_META_KEY = "CLASS_META_KEY";

    @Override
    public Resource deserialize(JsonElement jsonElement, Type type, JsonDeserializationContext jsonDeserializationContext) throws JsonParseException {
        JsonObject o = jsonElement.getAsJsonObject();
        String className = o.get(CLASS_META_KEY).getAsString();
        try {
            Class<?> cls = Class.forName(className);
            return jsonDeserializationContext.deserialize(jsonElement, cls);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public JsonElement serialize(Resource resource, Type type, JsonSerializationContext jsonSerializationContext) {
        Gson gson = new Gson();
        gson.toJson(resource, resource.getClass());
        JsonElement jsonElement = gson.toJsonTree(resource);
        jsonElement.getAsJsonObject().addProperty(CLASS_META_KEY, resource.getClass().getCanonicalName());
        return jsonElement;
    }
}
