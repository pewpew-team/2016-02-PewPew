package com.pewpew.pewpew.servelet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.jetbrains.annotations.NotNull;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

public class ScoreboardService extends HttpServlet {
    @NotNull
    private MongoModule mongoModule = MongoModule.getInstanse();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            List<User> query = mongoModule.provideDatastore().find(User.class).order("-rating").limit(5).asList();
            User[] users = query.toArray(new User[query.size()]);

            Gson gson = new Gson();
            String stringResponse = gson.toJson(users);

            response.setStatus(HttpServletResponse.SC_OK);
            response.setContentType("application/json; charset=utf-8");
            response.getWriter().println(stringResponse);
        } catch (JsonSyntaxException error) {
            System.err.println(error);
            ResponseManager.errorResponse("Cannot serilized Json", response);
        }
    }
    }

