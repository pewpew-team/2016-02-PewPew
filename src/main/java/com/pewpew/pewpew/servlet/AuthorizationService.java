package com.pewpew.pewpew.servlet;


import com.google.gson.Gson;

import com.google.gson.JsonObject;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.common.Validate;
import com.pewpew.pewpew.common.CookieHelper;
import com.pewpew.pewpew.common.JsonHelper;
import com.pewpew.pewpew.common.ResponseHelper;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.common.Settings;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class AuthorizationService extends HttpServlet {
    private AccountService accountService = new AccountService();

    public AuthorizationService(AccountService accountService) {
        this.accountService = accountService;
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = CookieHelper.getCockie(request, "token");
        if (cookie == null) {
            ResponseHelper.errorResponse("User unauth", response, Settings.UNAUTHORIZED);
            return;
        }
        Gson gson = new Gson();
        User user = accountService.getUserByToken(cookie.getValue());
        if (user == null) {
            ResponseHelper.errorResponse("User unauth", response, Settings.UNAUTHORIZED);
            return;
        }
        ObjectId userId = user.getId();
        String stringResponse = JsonHelper.createJsonWithId(userId);
        ResponseHelper.successResponse(stringResponse, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseHelper.errorResponse("Error reading input stream", response, Settings.INTERNAL_ERROR);
            return;
        }
        Cookie cookie = CookieHelper.getCockie(request, "token");
        Gson gson = new Gson();
        if (cookie == null) {
            User authUser = JsonHelper.getUserOutOfJson(jsonBuffer.toString());
            if (authUser == null) {
                ResponseHelper.errorResponse("Cannot serilized Json", response, Settings.INTERNAL_ERROR);
            }
            if (!Validate.userAuth(authUser)) {
                ResponseHelper.errorResponse("Some fiels is missing", response, Settings.BAD_REQUEST);
                return;
            }
            User user = MongoManager.getUser(authUser.getLogin(), authUser.getPassword());
            if (user == null) {
                ResponseHelper.errorResponse("Wrong login or password", response, Settings.BAD_REQUEST);
                return;
            }
            String token = UUID.randomUUID().toString();
            accountService.addToken(token, user);
            cookie = new Cookie("token", token);
            response.addCookie(cookie);
        }

        ObjectId userId = accountService.getUserByToken(cookie.getValue()).getId();

        String stringResponse = JsonHelper.createJsonWithId(userId);
        ResponseHelper.successResponse(stringResponse, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cockie = CookieHelper.getCockie(request, "token");
        if (cockie == null) {
            ResponseHelper.errorResponse("User unauth", response, Settings.UNAUTHORIZED);
            return;
        }
        if (accountService.closeToken(cockie.getValue())) {
            ResponseHelper.errorResponse("No active session with such token", response, Settings.BAD_REQUEST);
            return;
        }
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("message", "Logout complete");
        String stringResponse = gson.toJson(jsonResponse);

        cockie.setValue(null);
        cockie.setMaxAge(0);
        response.addCookie(cockie);

        ResponseHelper.successResponse(stringResponse, response);
    }
}
