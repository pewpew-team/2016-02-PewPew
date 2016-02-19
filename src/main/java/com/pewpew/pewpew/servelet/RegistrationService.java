package com.pewpew.pewpew.servelet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
import com.pewpew.pewpew.additional.Validate;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import jdk.nashorn.internal.parser.JSONParser;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.UUID;

public class RegistrationService extends HttpServlet {

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        StringBuffer jsonBuffer = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null) {
                jsonBuffer.append(line);
            }
        } catch (Exception e) {
            System.err.println(e.toString());
            response.setStatus(400);
            return;
        }

        Gson gson = new Gson();
        User user = gson.fromJson(jsonBuffer.toString(), User.class);

        if (!Validate.user(user)) {
            response.setStatus(400);
            return;
        }

        MongoModule mongoModule = MongoModule.getInstanse();
        User sameUser = mongoModule.provideDatastore().find(
                User.class, "email", user.getEmail()).get();
        
        if (sameUser != null) {
            response.setStatus(403);
            return;
        }

        String newToken = UUID.randomUUID().toString();
        user.setToken(newToken);
        mongoModule.provideDatastore().save(user);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("token", newToken);
        String stringResponse = gson.toJson(jsonResponse);

        response.setStatus(200);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(stringResponse);
    }
}
