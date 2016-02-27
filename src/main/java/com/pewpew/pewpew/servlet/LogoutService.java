package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.common.CockieHelper;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LogoutService extends HttpServlet {
    private AccountService accountService = new AccountService();

    public LogoutService(AccountService accountService) {
        this.accountService = accountService;
    }
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cockie = CockieHelper.getCockie(request, "token");
        if (cockie == null) {
            ResponseManager.errorResponse("User unauth", response, Settings.UNAUTHORIZED);
            return;
        }
        if (accountService.closeToken(cockie.getValue())) {
            ResponseManager.errorResponse("No active session with such token", response, Settings.BAD_REQUEST);
            return;
        }
        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("message", "Logout complete");
        String stringResponse = gson.toJson(jsonResponse);

        ResponseManager.successResponse(stringResponse, response);
    }
}
