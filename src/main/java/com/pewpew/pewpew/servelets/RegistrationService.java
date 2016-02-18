package com.pewpew.pewpew.Servelets;

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
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = request.getReader();
            while ((line = reader.readLine()) != null)
                jb.append(line);
        } catch (Exception e) { /*report an error*/ }

//        JSONObject jsonRequest = new JSONObject(jb.toString());
//        JSONObject jsonResponse = new JSONObject();
        Map<String, Object> responseMap =  new HashMap<>();

        email = request.getParameter("email");
        password = request.getParameter("password");

        if (email == null || email.isEmpty() || password == null || password.isEmpty()) {
            response.setStatus(400);
        }

        response.setContentType("application/json; charset=utf-8");
    }
}
