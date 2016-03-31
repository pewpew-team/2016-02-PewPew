package com.pewpew.pewpew.serialize;

import com.google.gson.*;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

public class ObjectIdDeserializer implements JsonDeserializer<ObjectId> {
    @Nullable
    @Override
    public ObjectId deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        JsonObject jsonObject = json.getAsJsonObject();
        ObjectId objectId = null;
        if (jsonObject.has("_id")) {
            objectId = new ObjectId(jsonObject.get("_id").getAsString());
        }
        return objectId;
    }
}
