package com.pewpew.pewpew;

import com.pewpew.pewpew.Model.User;
import com.pewpew.pewpew.Mongo.MongoModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

/**
 * Created by Leman on 18.02.16.
 */

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        MongoModule mongoModule = MongoModule.getInstanse();
        User user = createTestUser();
        mongoModule.provideDatastore().save(user);

        server.start();
        server.join();
    }

    private static User createTestUser() {
        User user = new User();
        user.setEmail("i_love_med@med.ru");
        user.setPassword("medmed");
        return user;
    }
}
