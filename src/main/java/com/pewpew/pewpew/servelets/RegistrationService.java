package com.pewpew.pewpew.Servelets;

import com.google.gson.Gson;
import com.pewpew.pewpew.Model.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class RegistrationService extends HttpServlet {
    private String email = "";
    private  String password = "";

    public RegistrationService() { }

    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        StringBuffer jsonBuffer = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jsonBuffer.append(line);
        } catch (Exception e) {
            System.err.println(e.toString());
        }

        Gson gson = new Gson();
        User user = gson.fromJson(jsonBuffer.toString(), User.class);

        if (user.getEmail() == null || user.getEmail().isEmpty()
                || user.getPassword().isEmpty()
                || user.getPassword() == null) {
            response.setStatus(400);
            return;
        }

        response.setStatus(200);
        response.setContentType("application/json; charset=utf-8");
    }
}
