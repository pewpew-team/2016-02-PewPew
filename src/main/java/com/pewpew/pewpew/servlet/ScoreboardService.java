package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.common.ResponseHelper;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.Datastore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ScoreboardService extends HttpServlet {

    @NotNull
    private final Datastore datastore = MongoModule.getInstanse().provideDatastore();

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        try {
            response.setStatus(HttpServletResponse.SC_OK);
            List<User> query = datastore.find(User.class).order("-rating").limit(5).asList();
            User[] users = query.toArray(new User[query.size()]);

            Gson gson = new Gson();
            String stringResponse = gson.toJson(users);

            ResponseHelper.successResponse(stringResponse, response);
        } catch (JsonSyntaxException error) {
            Logger log = Logger.getLogger(ScoreboardService.class.getName());
            log.log(Level.WARNING, "Got an exception.", error);
            ResponseHelper.errorResponse("Cannot serilized Json", response, Settings.INTERNAL_ERROR);
        }
    }


}

