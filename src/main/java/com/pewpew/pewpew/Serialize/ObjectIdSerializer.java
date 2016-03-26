package com.pewpew.pewpew.serialize;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import org.bson.types.ObjectId;


import java.lang.reflect.Type;

public class ObjectIdSerializer implements JsonSerializer<ObjectId> {

    @Override
    public JsonElement serialize(ObjectId src, Type typeOfSrc, JsonSerializationContext context) {
        JsonObject jsonObject = new JsonObject();
        if (src != null) {
            jsonObject.addProperty("_id", src.toString());
        }
        return jsonObject;
    }
}
