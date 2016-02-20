package com.pewpew.pewpew.additional;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import org.bson.types.ObjectId;

import java.io.IOException;

public class ObjectIdTypeAdapter extends TypeAdapter<ObjectId> {
    @Override
    public void write(final JsonWriter out, final ObjectId value) throws IOException {
        out.beginObject()
                .name("$oid")
                .value(value.toString())
                .endObject();
    }

    @Override
    public ObjectId read(final JsonReader in) throws IOException {
        in.beginObject();
//        assert "$oid".equals(in.nextName());
        String objectIdString = in.nextString();
        in.endObject();
        return new ObjectId(objectIdString);
    }
}