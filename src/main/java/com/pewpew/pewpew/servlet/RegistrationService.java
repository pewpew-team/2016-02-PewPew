package com.pewpew.pewpew.servlet;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;
import com.pewpew.pewpew.additional.BufferRead;
import com.pewpew.pewpew.common.Validate;
import com.pewpew.pewpew.common.JsonHelper;
import com.pewpew.pewpew.common.ResponseHelper;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.mongodb.morphia.Datastore;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

public class RegistrationService extends HttpServlet {
    private final Datastore datastore = MongoModule.getInstanse().provideDatastore();
    private AccountService accountService = new AccountService();

    public RegistrationService(AccountService accountService) {
        this.accountService = accountService;
    }


    @Override
    public void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        BufferRead bufferRead = new BufferRead(request);
        StringBuffer jsonBuffer = bufferRead.getStringBuffer();
        if (jsonBuffer == null) {
            ResponseHelper.errorResponse("Error reading input stream", response, Settings.INTERNAL_ERROR);
            return;
        }
        Gson gson = new Gson();
        try {
            User user = gson.fromJson(jsonBuffer.toString(), User.class);

            if (!Validate.userRegister(user)) {
                ResponseHelper.errorResponse("Some fiels is missing", response, Settings.FORBIDDEN);
                return;
            }

            if (!MongoManager.userExist(user)) {
                ResponseHelper.errorResponse("User already exist",response, Settings.FORBIDDEN);
                return;
            }

            String newToken = UUID.randomUUID().toString();
            accountService.addToken(newToken, user);
            datastore.save(user);

            Cookie cookie = new Cookie("token", newToken);
            response.addCookie(cookie);

            String stringResponse = JsonHelper.createJsonWithId(user.getId());
            ResponseHelper.successResponse(stringResponse, response);
        } catch (JsonSyntaxException error) {
            Logger log = Logger.getLogger(RegistrationService.class.getName());
            log.log(Level.WARNING, "Got an exception.", error);
            ResponseHelper.errorResponse("Cannot serilized Json", response, Settings.INTERNAL_ERROR);
        }
    }
}
