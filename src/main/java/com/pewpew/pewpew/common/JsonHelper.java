package com.pewpew.pewpew.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class JsonHelper {

    @Nullable
    public static User getUserOutOfJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, User.class);
        } catch(JsonSyntaxException error) {
            System.err.println(error);
            return null;
        }
    }

    @Nullable
    public static ObjectId getUserIdOfJson(String json) {
        Gson gson = new Gson();
        try {
            return gson.fromJson(json, MongoId.class).get_id();
        } catch(JsonSyntaxException error) {
            System.err.println(error);
            return null;
        }
    }

    @NotNull
    public static String createJsonWithId(ObjectId userId) {
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("id", userId.toString());
        return gson.toJson(jsonResponse);
    }
}
