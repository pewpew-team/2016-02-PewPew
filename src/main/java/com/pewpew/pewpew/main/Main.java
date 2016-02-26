package com.pewpew.pewpew.main;

import com.pewpew.pewpew.mongo.MongoModule;
import com.pewpew.pewpew.servlet.AuthorizationService;
import com.pewpew.pewpew.servlet.GetUserService;
import com.pewpew.pewpew.servlet.RegistrationService;
import com.pewpew.pewpew.servlet.ScoreboardService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception, InterruptedException {
        int port = 8080;
        if (args.length == 1) {
            String portString = args[0];
            port = Integer.valueOf(portString);
        }

        Server server = new Server(port);
        MongoModule mongoModule = MongoModule.getInstanse();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        RegistrationService registrationService = new RegistrationService();
        context.addServlet(new ServletHolder(registrationService),"/register");

        AuthorizationService authorizationService = new AuthorizationService();
        context.addServlet(new ServletHolder(authorizationService), "/auth");

        ScoreboardService scoreboardService = new ScoreboardService();
        context.addServlet(new ServletHolder(scoreboardService), "/scoreboard");

        GetUserService getUserService = new GetUserService();
        context.addServlet(new ServletHolder(getUserService), "/getUser");

        server.setHandler(context);
        server.start();
        server.join();
    }
}

// curl -H "Content-Type: application/json" -X POST -d '{"email":"xyz","password":"xyz"}' http://localhost:8080/auth
// curl -i -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:8080/scoreboard
// curl -H "Content-Type: application/json" -X POST -d "{"_id":"56c77735f6ca4379ec6e5898"}" http://localhost:8080/getUser


