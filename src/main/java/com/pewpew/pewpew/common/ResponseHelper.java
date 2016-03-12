package com.pewpew.pewpew.common;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ResponseHelper {

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
            }
        } catch (JsonSyntaxException error) {
            Logger log = Logger.getLogger(ResponseHelper.class.getName());
            log.log(Level.WARNING, "Got an exception.", error);
            ResponseHelper.errorResponse("Cannot serilized Json", response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
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
