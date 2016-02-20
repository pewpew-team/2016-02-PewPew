package com.pewpew.pewpew.servelet;

import com.google.gson.*;
import com.google.gson.annotations.SerializedName;
import com.google.gson.stream.JsonReader;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.additional.ObjectIdTypeAdapter;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.StringReader;


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
        JsonReader reader = new JsonReader(new StringReader(jsonBuffer.toString()));
        reader.setLenient(true);
        try {
            String jsonString = jsonBuffer.toString();
            idFromJson userIdString = gson.fromJson(jsonString, idFromJson.class);
            ObjectId userId = new ObjectId(userIdString.get_id());
            User user = MongoManager.getUser(userId);
            if (user == null) {
                ResponseManager.errorResponse("User does not exist", response);
                return;
            }

            String stringResponse = gson.toJson(user);
            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(stringResponse);
        } catch (JsonSyntaxException e) {
            ResponseManager.errorResponse("Cannot serilized Json",response);
            e.printStackTrace();
        }
    }
}

class idFromJson {

    public String get_id() {
        return _id;
    }
    @SerializedName("_id")
    public String _id;
}