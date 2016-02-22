package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.additional.Validate;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.mongodb.morphia.Datastore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class RegistrationService extends HttpServlet {
    private Datastore ds = MongoModule.getInstanse().provideDatastore(Settings.USERS_COLLECTION, Settings.MODEL_PACKAGE);

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseManager.errorResponse("Error reading input stream", response);
            return;
        }
        Gson gson = new Gson();
        try {
            User user = gson.fromJson(jsonBuffer.toString(), User.class);

            if (!Validate.user(user)) {
                ResponseManager.errorResponse("Some fiels is missing", response);
                return;
            }

            if (!MongoManager.userExist(user)) {
                ResponseManager.errorResponse("User already exist", response);
                return;
            }

            String newToken = UUID.randomUUID().toString();
            user.setToken(newToken);
            ds.save(user);

            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("token", newToken);
            String stringResponse = gson.toJson(jsonResponse);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(stringResponse);
        } catch (JsonSyntaxException error) {
            System.err.println(error);
            ResponseManager.errorResponse("Cannot serilized Json", response);
        }
    }
}
