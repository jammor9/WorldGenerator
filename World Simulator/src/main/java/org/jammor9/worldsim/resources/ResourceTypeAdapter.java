package org.jammor9.worldsim.resources;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

import java.io.IOException;

public class ResourceTypeAdapter extends TypeAdapter<Resource> {
    @Override
    public void write(JsonWriter jsonWriter, Resource resource) throws IOException {
        jsonWriter.value(resource.getClass().toString());
    }

    @Override
    public Resource read(JsonReader jsonReader) throws IOException {
        return null;
    }
}
