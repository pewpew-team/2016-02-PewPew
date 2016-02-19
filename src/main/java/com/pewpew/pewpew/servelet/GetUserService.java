package com.pewpew.pewpew.servelet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.bson.types.ObjectId;
import org.mongodb.morphia.dao.BasicDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class GetUserService extends HttpServlet {
    private MongoModule mongoModule = MongoModule.getInstanse();

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseManager.errorResponse("Error reading input stream", response);
            return;
        }
        Gson gson = new Gson();
        ObjectId userId = gson.fromJson(jsonBuffer.toString(), ObjectId.class);

        User user = MongoManager.getUser(userId);

        if (user == null) {
            ResponseManager.errorResponse("User does not exist", response);
            return;
        }

        String stringResponse = gson.toJson(user);

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=utf-8");
        response.getWriter().println(stringResponse);
    }
}
