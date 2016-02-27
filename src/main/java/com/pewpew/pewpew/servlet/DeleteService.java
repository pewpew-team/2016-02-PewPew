package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.common.JsonHelper;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import org.bson.types.ObjectId;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class DeleteService extends HttpServlet{
    private AccountService accountService = new AccountService();

    public DeleteService(AccountService accountService) {
        this.accountService = accountService;
    }

    @Override
    protected void doDelete(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseManager.errorResponse("Error reading input stream", response, Settings.INTERNAL_ERROR);
            return;
        }
        ObjectId userId = JsonHelper.getUserIdOfJson(jsonBuffer.toString());
        if (userId == null) {
            ResponseManager.errorResponse("Cannot serilized Json", response, Settings.INTERNAL_ERROR);
            return;
        }
        User user = MongoManager.getUser(userId);
        if (user == null) {
            ResponseManager.errorResponse("User does not exist", response, Settings.BAD_REQUEST);
            return;
        }
        MongoManager.delete(user);
        accountService.deleteUser(user);

        Gson gson = new Gson();
        JsonObject jsonResponse = new JsonObject();
        jsonResponse.addProperty("Message", "User auth complete");
        String stringResponse = gson.toJson(jsonResponse);

        ResponseManager.successResponse(stringResponse, response);
    }
}
