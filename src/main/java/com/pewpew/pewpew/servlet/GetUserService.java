package com.pewpew.pewpew.servlet;


import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.common.CockieHelper;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;


public class GetUserService extends HttpServlet {
    private MongoModule mongoModule = MongoModule.getInstanse();
    private AccountService accountService = new AccountService();

    public GetUserService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cockie = CockieHelper.getCockie(request, "token");
        if (cockie == null) {
            ResponseManager.errorResponse("User unauth", response, Settings.UNAUTHORIZED);
            return;
        }
        User user = accountService.getUserByToken(cockie.getValue());
        if (user == null) {
            ResponseManager.errorResponse("User unauth or token expired", response, Settings.BAD_REQUEST);
            return;
        }

        Gson gson = new Gson();
        String stringResponse = gson.toJson(user);

        ResponseManager.successResponse(stringResponse, response);
    }

}
