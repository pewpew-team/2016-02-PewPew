package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.additional.Validate;
import com.pewpew.pewpew.common.CockieHelper;
import com.pewpew.pewpew.common.JsonHelper;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class EditServiece extends HttpServlet{
    private AccountService accountService = new AccountService();

    public EditServiece(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doPut(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        Cookie cockie = CockieHelper.getCockie(request, "token");
        Gson gson = new Gson();
        if (cockie == null) {
            ResponseManager.errorResponse("User is unauth", response, Settings.UNAUTHORIZED);
            return;
        }
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseManager.errorResponse("Error reading input stream", response, Settings.INTERNAL_ERROR);
            return;
        }
        User editUser = JsonHelper.getUserOutOfJson(jsonBuffer.toString());
        if (editUser == null) {
            ResponseManager.errorResponse("Cannot serilized Json", response, Settings.INTERNAL_ERROR);
        }
        User user = accountService.getUserByToken(cockie.getValue());
        if (Validate.validateField(editUser.getLogin())) {
            user.setLogin(editUser.getLogin());
        }
        if (Validate.validateField(editUser.getEmail())) {
            user.setLogin(editUser.getEmail());
        }
        if (Validate.validateField(editUser.getPassword())) {
            user.setLogin(editUser.getPassword());
        }
        accountService.updateUser(cockie.getValue(), user);

        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("Message", "User update complite");
        String stringResponse = gson.toJson(jsonResponse);

        ResponseManager.successResponse(stringResponse, response);
    }
}
