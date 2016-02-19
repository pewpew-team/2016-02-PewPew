package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import com.pewpew.pewpew.servelet.AuthorizationService;
import com.pewpew.pewpew.servelet.RegistrationService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception, InterruptedException {
        Server server = new Server(8080);
        MongoModule mongoModule = MongoModule.getInstanse();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        RegistrationService registrationService = new RegistrationService();
        context.addServlet(new ServletHolder(registrationService),"/register");

        AuthorizationService authorizationService = new AuthorizationService();
        context.addServlet(new ServletHolder(authorizationService), "/auth");
        server.setHandler(context);
        server.start();
        server.join();
    }
}

// curl -H "Content-Type: application/json" -X POST -d '{"email":"xyz","password":"xyz"}' http://localhost:8080/auth


