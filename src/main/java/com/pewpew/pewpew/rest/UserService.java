package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.main.AccountServiceImpl;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.annotations.ValidForCreation;
import com.pewpew.pewpew.annotations.ValidForModification;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Singleton
@Path("/user")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class UserService {
    @Inject
    private com.pewpew.pewpew.main.Context context;

    public UserService() {}

    @POST
    public Response signUp(@ValidForCreation User user, @Context HttpHeaders headers,
                           @CookieParam("token") String token) {
        AccountService accountService = context.get(AccountService.class);
        System.out.print("Got request: createUser \n");
        if (!accountService.userExists(user)) {
            return Response.status(Response.Status.CONFLICT).build();
        }
        String newToken = UUID.randomUUID().toString();
        accountService.addToken(newToken, user);
        accountService.addUser(user);
        NewCookie cookie = new NewCookie("token", newToken);
        System.out.print("Putting token into cookie \n");
        return Response.ok(Response.Status.OK).cookie(cookie).entity(user.getId()).build();
    }


    @GET
    @Path("{id}")
    public Response userInfo(@PathParam("id") String userId,
                             @CookieParam("token") String token) {
        AccountService accountService = context.get(AccountService.class);

        System.out.print("Got request: userInfo with id"  + userId + '\n');
        User userProfile = accountService.getUserByToken(token);
        if (userProfile != null) {
            System.out.print("Putting userInfo in json \n");
            return Response.ok(Response.Status.OK).entity(userProfile).build();
        }
        System.out.print("User not found \n");
        return Response.status(Response.Status.CONFLICT).build();
    }

    @PUT
    @Path("{id}")
    public Response changeUserInfo(@PathParam("id") String userId,
                                   @ValidForModification User editedUser, @CookieParam("token") String token) {
        AccountService accountService = context.get(AccountService.class);

        System.out.print("Got request: changeUserInfo"  + userId + '\n');
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
        accountService.addUser(activeUser);
        return Response.ok(Response.Status.OK).entity(activeUser.getId()).build();
    }

    @DELETE
    @Path("{id}")
    public Response deleteUser(@PathParam("id") String userId) {
        AccountService accountService = context.get(AccountService.class);

        User user = accountService.getUserById(userId);
        if (user == null) {
            return Response.status(Response.Status.FORBIDDEN).build();
        }
        accountService.delete(user);
        accountService.deleteUser(user);
        return Response.ok(Response.Status.OK).build();
    }
}