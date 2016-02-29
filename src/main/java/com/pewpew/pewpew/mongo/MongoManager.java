package com.pewpew.pewpew.mongo;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;

public class MongoManager {
    private static final MongoModule mongoModule = MongoModule.getInstanse();
    private static final Datastore ds = mongoModule.provideDatastore();

    @Nullable
    public static User getUser(String email) {
        return ds.find(User.class, "email", email).get();
    }

    @Nullable
    public static User getUser(String login, String password) {
        return ds.find(User.class, "login", login).field("password").equal(password).get();
    }

    @Nullable
    public static User getUser(ObjectId userId) {
        return ds.get(User.class, userId);
    }

    @NotNull
    public static Boolean userExist(User newUser) {
        User user = getUser(newUser.getEmail());
        return user == null;
    }

    public static void delete(User user) {
        ds.delete(user);
    }
}
