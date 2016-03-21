package com.pewpew.pewpew.rest;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.jetbrains.annotations.NotNull;
import org.mongodb.morphia.Datastore;
import javax.ws.rs.GET;
import javax.ws.rs.core.Response;
import java.util.List;

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

