package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.common.Validate;
import com.pewpew.pewpew.common.CookieHelper;
import com.pewpew.pewpew.common.JsonHelper;
import com.pewpew.pewpew.common.ResponseHelper;
import com.pewpew.pewpew.model.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserService extends HttpServlet {
    private AccountService accountService = new AccountService();
    private final Datastore datastore = MongoModule.getInstanse().provideDatastore();

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cockie = CookieHelper.getCockie(request);
        Gson gson = new Gson();
        if (cockie == null) {
            ResponseHelper.errorResponse("User is unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseHelper.errorResponse("Error reading input stream", response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        User editUser = JsonHelper.getUserOutOfJson(jsonBuffer.toString());
        if (editUser == null) {
            ResponseHelper.errorResponse("Cannot serilized Json", response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        User user = accountService.getUserByToken(cockie.getValue());
        if (user == null)  {
            ResponseHelper.errorResponse("User is unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (Validate.validateField(editUser.getLogin())) {
            user.setLogin(editUser.getLogin());
        }
        if (Validate.validateField(editUser.getEmail())) {
            user.setEmail(editUser.getEmail());
        }
        if (Validate.validateField(editUser.getPassword())) {
            user.setPassword(editUser.getPassword());
        }
        if (!accountService.updateUser(cockie.getValue(), user)) {
            ResponseHelper.errorResponse("User not updated", response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
        datastore.save(user);
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("Message", "User update complite");
        String stringResponse = gson.toJson(jsonResponse);
        ResponseHelper.successResponse(stringResponse, response);
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cockie = CookieHelper.getCockie(request);
        if (cockie == null) {
            ResponseHelper.errorResponse("User unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        User user = accountService.getUserByToken(cockie.getValue());
        if (user == null) {
            ResponseHelper.errorResponse("User unauth or token expired", response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        Gson gson = new Gson();
        String stringResponse = gson.toJson(user);

        ResponseHelper.successResponse(stringResponse, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String pathInfo = request.getPathInfo();
        String[] pathParts = pathInfo.split("/");
        ObjectId userId = new ObjectId(pathParts[1]);

        User user = MongoManager.getUser(userId);
        if (user == null) {
            ResponseHelper.errorResponse("User does not exist", response, HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        MongoManager.delete(user);
        accountService.deleteUser(user);

        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("Message", "User delete complete");
        String stringResponse = gson.toJson(jsonResponse);

        ResponseHelper.successResponse(stringResponse, response);
    }

}
