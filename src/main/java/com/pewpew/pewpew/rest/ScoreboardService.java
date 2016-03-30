package com.pewpew.pewpew.rest;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonPrimitive;
import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.main.Context;
import com.pewpew.pewpew.model.User;


import javax.inject.Inject;
import javax.inject.Singleton;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.List;

@Singleton
@Path("/scoreboard")
@Consumes(MediaType.APPLICATION_JSON)
@Produces(MediaType.APPLICATION_JSON)
public class ScoreboardService {
    @Inject
    private Context context;

    @GET
    public Response getScoreboard() {
        final AccountService accountService = context.get(AccountService.class);
        List<User> query = accountService.getTop();
        User[] users = query.toArray(new User[query.size()]);
        return Response.ok(Response.Status.OK).entity(new Users(users)).build();
    }

    private class Users {
        private User[] scores;
        public Users(User[] scores) {
            this.scores = scores;
        }
    }

}

