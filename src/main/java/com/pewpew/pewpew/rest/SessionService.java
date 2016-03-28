package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.main.*;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.annotations.ValidForLogin;

import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import javax.ws.rs.core.Context;
import java.util.UUID;

@Singleton
@Path("/session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SessionService {
    @Inject
    private com.pewpew.pewpew.main.Context context;

    @POST
//    @Consumes(MediaType.APPLICATION_JSON)
//    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(@ValidForLogin User authUser, @Context HttpHeaders headers,
                           @CookieParam("token") String token,
                           @CookieParam("token") Cookie cook) {
        AccountService accountService = context.get(AccountService.class);
        System.out.print("Got request: authUser \n");
        if (token == null || token.isEmpty()) {
            User user = accountService.getUser(authUser.getLogin(), authUser.getPassword());
            if (user == null) {
                return Response.status(Response.Status.FORBIDDEN).build();
            }
            token = UUID.randomUUID().toString();
            accountService.addToken(token, user);
            NewCookie cookie = new NewCookie("token", token);
            System.out.print("Putting token into cookie \n");
            return Response.ok(Response.Status.OK).cookie(cookie).entity(user.getId()).build();
        }
        User userFromToken = accountService.getUserByToken(token);
        if (userFromToken == null) {
            System.out.print("User have cookie, but not auth \n");
            NewCookie newCookie = new NewCookie(cook, null, 0, false);
            return Response.status(Response.Status.UNAUTHORIZED).cookie(newCookie).build();
        }
        System.out.print("Sending user info from logged user \n");
        return Response.ok(Response.Status.OK).entity(userFromToken.getId()).build();
    }

    @GET
    public Response checkUserAuth(@CookieParam("token") String token) {
        final AccountService accountService = context.get(AccountService.class);
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        final User user = accountService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(Response.Status.OK).entity(user.getId()).build();
    }

    @DELETE
    public Response signOut(@CookieParam("token") Cookie cookie) {
        final AccountService accountService = context.get(AccountService.class);
        System.out.print("Got request: cancel token" + '\n');
        if (cookie == null) {
            System.out.print("Cookie is null" + '\n');
            return Response.ok().build();
        }
        if (accountService.closeToken(cookie.getValue())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        NewCookie newCookie = new NewCookie(cookie, null, 0, false);
        System.out.print("Putting empty token" + '\n');
        return Response.ok().cookie(newCookie).build();
    }
}