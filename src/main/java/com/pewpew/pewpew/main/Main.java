package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import com.pewpew.pewpew.servelet.RegistrationService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception {
        Server server = new Server(8080);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        RegistrationService registrationService = new RegistrationService();
        context.addServlet(new ServletHolder(registrationService),"/register");
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

// curl -H "Content-Type: application/json" -X POST -d '{"email":"xyz","password":"xyz"}' http://localhost:8080/register


