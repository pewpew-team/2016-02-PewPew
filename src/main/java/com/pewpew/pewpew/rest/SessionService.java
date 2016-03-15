package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.annotations.ValidForLogin;
import com.pewpew.pewpew.mongo.MongoManager;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.*;
import java.util.UUID;

@Singleton
@Path("/session")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class SessionService {
    private final AccountService accountService;

    public SessionService() {
        this.accountService = com.pewpew.pewpew.main.RestApplication.ACCOUNT_SERVICE;
    }

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signIn(@ValidForLogin User authUser, @Context HttpHeaders headers,
                           @CookieParam("token") String token) {
        if (authUser != null) {
            if (token == null || token.isEmpty()) {
                User user = MongoManager.getUser(authUser.getLogin(), authUser.getPassword());
                if (user == null) {
                    return Response.status(Response.Status.FORBIDDEN).build();
                }
                token = UUID.randomUUID().toString();
                accountService.addToken(token, user);
                NewCookie cookie = new NewCookie("token", token);
                return Response.ok(Response.Status.OK).cookie(cookie).entity(user.getId().toString()).build();
            }
            User userFromToken = accountService.getUserByToken(token);
            if (userFromToken == null) {
                return Response.status(Response.Status.UNAUTHORIZED).build();
            }
            return Response.ok(Response.Status.OK).entity(userFromToken.getId()).build();
        }
        return Response.status(Response.Status.FORBIDDEN).build();
    }

    @GET
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response checkUserAuth(@CookieParam("token") String token) {
        if (token == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        User user = accountService.getUserByToken(token);
        if (user == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        return Response.ok(Response.Status.OK).entity(user.getId()).build();
    }

    @DELETE
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response signOut(@CookieParam("token") Cookie cookie) {
        if (cookie == null) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        if (accountService.closeToken(cookie.getValue())) {
            return Response.status(Response.Status.UNAUTHORIZED).build();
        }
        NewCookie newCookie = new NewCookie(cookie, null, 0, false);
        return Response.ok("OK").cookie(newCookie).build();
    }

}
