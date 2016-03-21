package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.Datastore;

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

    @NotNull
    private final Datastore datastore = MongoModule.getInstanse().provideDatastore();

    @GET
    public Response getScoreboard() {
        List<User> query = datastore.find(User.class).order("-rating").limit(8).asList();
        User[] users = query.toArray(new User[query.size()]);
        return Response.ok(Response.Status.OK).entity(users).build();
    }
}

