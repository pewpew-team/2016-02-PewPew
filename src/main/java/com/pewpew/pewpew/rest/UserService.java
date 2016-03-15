package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.annotations.UserInfo;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoManager;
import com.pewpew.pewpew.mongo.MongoModule;
import org.bson.types.ObjectId;
import org.mongodb.morphia.Datastore;

import javax.inject.Singleton;
import javax.validation.Valid;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Singleton
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
    private final Datastore datastore = MongoModule.getInstanse().provideDatastore();
    private AccountService accountService;

    public UserService(AccountService accountService) {
        this.accountService = accountService;
    }

    @POST
    public Response signUp(@Valid User user, @Context HttpHeaders headers,
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

    @UserInfo
    @GET
    @Path("{id}")
    public Response userInfo(@PathParam("id") String userId,
                             @CookieParam("token") String token) {
        User userProfile = accountService.getUserByToken(token);
        if (userProfile != null) {
            return Response.ok(Response.Status.OK).entity(userProfile).build();
        }
        return Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{id}")
    public Response changeUserInfo(User editedUser, @CookieParam("token") String token) {
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
        return Response.ok(Response.Status.OK).entity(activeUser).build();
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
