package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.User;
import com.pewpew.pewpew.mongo.MongoModule;
import com.pewpew.pewpew.servelet.AuthorizationService;
import com.pewpew.pewpew.servelet.GetUserService;
import com.pewpew.pewpew.servelet.RegistrationService;
import com.pewpew.pewpew.servelet.ScoreboardService;
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
// curl -H "Content-Type: application/json" -X POST -d '{"id":"56c77735f6ca4379ec6e5895"}' http://localhost:8080/getUser
// curl -H "Content-Type: application/json" -X POST -d {\"_id\":{\"$oid\":\"51eae100c2e6b6c222ec3431\"}} http://localhost:8080/getUser
// curl -H "Content-Type: application/json" -X POST -d "{"_id":"56c77735f6ca4379ec6e5898"}" http://localhost:8080/getUser


