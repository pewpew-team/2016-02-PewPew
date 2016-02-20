package com.pewpew.pewpew.servelet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ResponseManager {

    public static void errorResponse(String errorText,
                                     HttpServletResponse response) {
        try {
            Gson gson = new Gson();
            JsonObject jsonResponse = new JsonObject();
            jsonResponse.addProperty("error", errorText);
            String stringResponse = gson.toJson(jsonResponse);
            response.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            response.setContentType("application/json; charset=utf-8");
            try {
                response.getWriter().println(stringResponse);
            } catch (IOException e) {
                System.err.println("Json cannot be send");
            /* FIX ME: Need to push error to top */
            }
        } catch (JsonSyntaxException error) {
            System.err.println(error);
            ResponseManager.errorResponse("Cannot serilized Json", response);
        }
    }
}
