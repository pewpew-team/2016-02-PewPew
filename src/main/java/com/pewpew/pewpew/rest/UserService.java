package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.annotations.UserInfo;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.annotations.ValidForCreation;
import com.pewpew.pewpew.annotations.ValidForModification;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.mongodb.morphia.Datastore;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.lang.annotation.Annotation;
import java.util.UUID;

@Singleton
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
    private final Datastore datastore = MongoModule.getInstanse().provideDatastore();
    private final AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    public Response signUp(@ValidForCreation User user, @Context HttpHeaders headers,
                           @CookieParam("token") String token) {
        if (!MongoManager.userExist(user)) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        String newToken = UUID.randomUUID().toString();
        accountService.addToken(newToken, user);
        datastore.save(user);
        NewCookie cookie = new NewCookie("token", newToken);
        return Response.ok(Response.Status.OK).cookie(cookie).entity(user.getId().toString()).build();
    }


    @GET
    @Path("{id}")
    @UserInfo
    public Response userInfo(@PathParam("id") String userId,
                             @CookieParam("token") String token) {
        User userProfile = accountService.getUserByToken(token);
        if (userProfile != null) {
            userProfile.setPassword(null);
            Response response = Response.ok(Response.Status.OK).entity(userProfile,
                    new Annotation[] {UserInfo.Factory.getInstance()}).build();
            return response;
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{id}")
    public Response changeUserInfo(@PathParam("id") String userId,
                                   @ValidForModification User editedUser, @CookieParam("token") String token) {
        User activeUser = accountService.getUserByToken(token);
        if (activeUser == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        if (editedUser.getEmail() != null) {
            activeUser.setEmail(editedUser.getEmail());
        }
        if (editedUser.getLogin() != null) {
            activeUser.setLogin(editedUser.getLogin());
        }
        if (editedUser.getPassword() != null) {
            activeUser.setPassword(editedUser.getPassword());
        }
        datastore.save(activeUser);
        return Response.ok(Response.Status.OK).entity(activeUser.getId().toString()).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") String userId) {
        User user = MongoManager.getUser(userId);
        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        MongoManager.delete(user);
        accountService.deleteUser(user);
        return Response.ok(Response.Status.OK).build();
    }
}