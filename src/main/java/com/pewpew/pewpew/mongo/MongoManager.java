package com.pewpew.pewpew.mongo;
import com.pewpew.pewpew.model.User;
import org.bson.types.ObjectId;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.mongodb.morphia.Datastore;

public class MongoManager {
    private static final MongoModule MONGO_MODULE = MongoModule.getInstanse();
    private static final Datastore DATASTORE = MONGO_MODULE.provideDatastore();

    @Nullable
    public static User getUser(String email) {
        return DATASTORE.find(User.class, "email", email).get();
    }

    @Nullable
    public static User getUser(String login, String password) {
        return DATASTORE.find(User.class, "login", login).field("password").equal(password).get();
    }

    @Nullable
    public static User getUser(ObjectId userId) {
        return DATASTORE.get(User.class, userId);
    }

    @NotNull
    public static Boolean userExist(User newUser) {
        User user = getUser(newUser.getEmail());
        return user == null;
    }

    public static void delete(User user) {
        DATASTORE.delete(user);
    }
}
