package com.pewpew.pewpew.serialize;

import com.google.gson.*;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;

import java.lang.reflect.Type;

public class UserDeserializer implements JsonDeserializer<User> {
    @Override
    public User deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        User user = new User();
        JsonObject jsonObject = json.getAsJsonObject();
        if (jsonObject.has("_id")) {
            user.setId(new ObjectId(jsonObject.get("_id").getAsString()));
        }
        if (jsonObject.has("password")) {
            user.setPassword(jsonObject.get("password").getAsString());
        }
        if (jsonObject.has("login")) {
            user.setLogin(jsonObject.get("login").getAsString());
        }
        if (jsonObject.has("email")) {
            user.setEmail(jsonObject.get("email").getAsString());
        }
        if (jsonObject.has("rating")) {
            user.setRating(jsonObject.get("rating").getAsInt());
        }

        return user;
    }
}
