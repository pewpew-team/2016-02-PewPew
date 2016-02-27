package com.pewpew.pewpew.servlet;


import com.google.gson.Gson;

import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.additional.Validate;
import com.pewpew.pewpew.common.CockieHelper;
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
        Cookie cockie = CockieHelper.getCockie(request, "token");
        if (cockie == null) {
            ResponseHelper.errorResponse("User unauth", response, Settings.UNAUTHORIZED);
            return;
        }
        Gson gson = new Gson();

        ObjectId userId = accountService.getUserByToken(cockie.getValue()).getId();
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
        Cookie cockie = CockieHelper.getCockie(request, "token");
        Gson gson = new Gson();
        if (cockie == null) {
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
            cockie = new Cookie("token", token);
            response.addCookie(cockie);
        }

        ObjectId userId = accountService.getUserByToken(cockie.getValue()).getId();

        String stringResponse = JsonHelper.createJsonWithId(userId);
        ResponseHelper.successResponse(stringResponse, response);
    }
}
