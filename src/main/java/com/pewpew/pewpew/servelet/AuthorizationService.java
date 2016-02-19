package com.pewpew.pewpew.servelet;


import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.additional.Validate;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class AuthorizationService extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            response.setStatus(403);
            return;
        }
        Gson gson = new Gson();
        User authUser = gson.fromJson(jsonBuffer.toString(), User.class);

        if (!Validate.user(authUser)) {
            response.setStatus(400);
            return;
        }
        MongoModule mongoModule = MongoModule.getInstanse();
        User user = mongoModule.provideDatastore().find(
                User.class, "email", authUser.getEmail()).get();
        if (user == null) {
            response.setStatus(403);
            return;
        }

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("token", user.getToken());
        String stringResponse = gson.toJson(jsonResponse);

        response.setStatus(200);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(stringResponse);
    }
}
