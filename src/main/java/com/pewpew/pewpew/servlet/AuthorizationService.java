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
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;

public class AuthorizationService extends HttpServlet {
    @SuppressWarnings("all")
    static final Logger logger = LoggerFactory.getLogger(AuthorizationService.class);
    private AccountService accountService;

    public AuthorizationService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cookie = CookieHelper.getCockie(request);
        if (cookie == null) {
            ResponseHelper.errorResponse("User unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        User user = accountService.getUserByToken(cookie.getValue());
        if (user == null) {
            ResponseHelper.errorResponse("User unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
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
            ResponseHelper.errorResponse("Error reading input stream", response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            return;
        }
        Cookie cookie = CookieHelper.getCockie(request);
        if (cookie == null) {
            User authUser = JsonHelper.getUserOutOfJson(jsonBuffer.toString());
            if (authUser == null) {
                ResponseHelper.errorResponse("User doesnot exist", response, HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                return;
            }
            if (!Validate.userAuth(authUser)) {
                ResponseHelper.errorResponse("Some fiels is missing", response, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            User user = MongoManager.getUser(authUser.getLogin(), authUser.getPassword());
            if (user == null) {
                ResponseHelper.errorResponse("Wrong login or password", response, HttpServletResponse.SC_BAD_REQUEST);
                return;
            }
            String token = UUID.randomUUID().toString();
            accountService.addToken(token, user);
            cookie = new Cookie("token", token);
            response.addCookie(cookie);
        }
        String tokenString = cookie.getValue();
        if (tokenString == null) {
            ResponseHelper.errorResponse("User unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        User userFromToken = accountService.getUserByToken(tokenString);
        if (userFromToken == null) {
            ResponseHelper.errorResponse("User unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        ObjectId userId = userFromToken.getId();

        String stringResponse = JsonHelper.createJsonWithId(userId);
        ResponseHelper.successResponse(stringResponse, response);
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cockie = CookieHelper.getCockie(request);
        if (cockie == null) {
            ResponseHelper.errorResponse("User unauth", response, HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        if (accountService.closeToken(cockie.getValue())) {
            ResponseHelper.errorResponse("No active session with such token", response, HttpServletResponse.SC_BAD_REQUEST);
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
