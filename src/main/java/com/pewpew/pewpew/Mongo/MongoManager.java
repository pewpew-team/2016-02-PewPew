package com.pewpew.pewpew.mongo;
import com.pewpew.pewpew.model.User;
//import org.jetbrains.annotations.NotNull;
//import org.jetbrains.annotations.Nullable;

public class MongoManager {

//    @Nullable
    public static User getUser(String email) {
        MongoModule mongoModule = MongoModule.getInstanse();
        User user = mongoModule.provideDatastore().find(
                User.class, "email", email).get();
        return user;
    }

//    @NotNull
    public static Boolean userExist(User user) {
        User oldUser = getUser(user.getEmail());
        if (oldUser == null) {
            return true;
        }
        return false;
    }
}
