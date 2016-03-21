package com.pewpew.pewpew.Serialize;

import com.google.gson.*;
import com.pewpew.pewpew.model.User;

import java.lang.reflect.Type;

public class UserSerializer implements JsonSerializer<User>{
    @Override
    public JsonElement serialize(User src, Type typeOfSrc, JsonSerializationContext context) {

        JsonObject jsonObject = new JsonObject();
        if (src.getId() != null) {
            jsonObject.addProperty("id", src.getId().toString());
        }
        if (src.getEmail() != null) {
            jsonObject.addProperty("email", src.getEmail());
        }
        if (src.getLogin() != null) {
            jsonObject.addProperty("login", src.getLogin());
        }
        if (src.getPassword() !=null) {
            jsonObject.addProperty("password", src.getPassword());
        }
        if(src.getRating() != null) {
            jsonObject.addProperty("rating", src.getRating());
        }

        return jsonObject;
    }
}


