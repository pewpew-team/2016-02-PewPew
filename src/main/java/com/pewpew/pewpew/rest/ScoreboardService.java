package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.main.AccountService;
import com.pewpew.pewpew.main.Context;
import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.model.Users;

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
        try {
            final List<User> query = accountService.getTop();
//            final JsonArray array = new JsonArray();
//            query.stream().forEach(user->array.add(new JsonObject(user)));
            return Response.ok(Response.Status.OK).entity(new Users(query)).build();
        } catch (RuntimeException e) {
            e.printStackTrace();
            return Response.status(Response.Status.NOT_IMPLEMENTED).build();
        }
    }
}

