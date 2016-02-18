package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        server.setHandler(context);
        MongoModule mongoModule = MongoModule.getInstanse();
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
