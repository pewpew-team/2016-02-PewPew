package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.annotations.UserInfo;
import com.pewpew.pewpew.common.ResponseHelper;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.common.Validate;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.mongodb.morphia.Datastore;

import javax.inject.Singleton;
import javax.servlet.http.Cookie;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Singleton
@Path("/user")
public class UserService {
    private final Datastore datastore = MongoModule.getInstanse().provideDatastore();
    private AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signUp(@Valid User user, @Context HttpHeaders headers,
                           @CookieParam("token") String token) {
        if (!MongoManager.userExist(user)) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        String newToken = UUID.randomUUID().toString();
        accountService.addToken(newToken, user);
        datastore.save(user);
        NewCookie cookie = new NewCookie("token", newToken);
        return Response.ok(Response.Status.OK).cookie(cookie).entity(user).build();
    }

    @UserInfo
    @GET
    @Path("{id}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response userInfo(@PathParam("id") String userId,
                             @CookieParam("token") String token) {
        final User userProfile = accountService.getUserByToken(token);
        if (userProfile != null) {
            return Response.ok(Response.Status.OK).entity(userProfile).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }
}
