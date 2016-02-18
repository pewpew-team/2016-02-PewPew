package com.pewpew.pewpew.servelet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.mongodb.MongoClient;
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
    private String email = "";
    private  String password = "";

    public RegistrationService() { }

    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuffer jsonBuffer = new StringBuffer();
        try {
            BufferedReader reader = request.getReader();
            String line = null;
            while ((line = reader.readLine()) != null)
                jsonBuffer.append(line);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        Gson gson = new Gson();
        User user = gson.fromJson(jsonBuffer.toString(), User.class);

        if (!validateUser(user)) {
            response.setStatus(400);
            return;
        }

        String newToken = UUID.randomUUID().toString();
        user.setToken(newToken);

        MongoModule mongoModule = MongoModule.getInstanse();
        mongoModule.provideDatastore().save(user);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("token", newToken);
        String stringResponse = gson.toJson(jsonResponse);

        response.setStatus(200);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(stringResponse);
    }

    private boolean validateUser(User user) {
        if (user.getEmail() == null) return false;
        if (user.getEmail().isEmpty()) return false;
        if (user.getPassword().isEmpty()) return false;
        if (user.getPassword() == null) return false;
        return true;
    }
}
