package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseManager {

    public static void errorResponse(String errorText,
                                     HttpServletResponse response, Integer errorStatus) {
        try {
            Gson gson = new Gson();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("error", errorText);
            String stringResponse = gson.toJson(jsonResponse);
            response.setStatus(errorStatus);
            response.setContentType("application/json; charset=utf-8");
            try {
                response.getWriter().println(stringResponse);
            } catch (IOException e) {
                System.err.println("Json cannot be send");
                return;
            /* FIX ME: Need to push error to top */
            }
        } catch (JsonSyntaxException error) {
            System.err.println(error);
            ResponseManager.errorResponse("Cannot serilized Json", response, 500);
            return;
        }
    }

    public static void successResponse(String json, HttpServletResponse response) {

        response.setStatus(HttpServletResponse.SC_OK);
        response.setContentType("application/json; charset=utf-8");
        try {
            response.getWriter().println(json);
        } catch (IOException e) {
            System.err.println("Json cannot be send");
        }
    }
}
