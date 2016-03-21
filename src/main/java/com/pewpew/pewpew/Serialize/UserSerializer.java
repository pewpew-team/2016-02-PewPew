package com.pewpew.pewpew.Serialize;

import com.google.gson.*;
import com.pewpew.pewpew.model.User;

import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User>{
    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();

        jsonObject.addProperty("id", src.getId().toString());
        jsonObject.addProperty("email", src.getEmail());
        jsonObject.addProperty("login", src.getLogin());
        jsonObject.addProperty("rating", src.getRating());

        return jsonObject;
    }
}


