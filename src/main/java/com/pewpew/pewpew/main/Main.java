package com.pewpew.pewpew.main;

import com.pewpew.pewpew.mongo.MongoModule;
import com.pewpew.pewpew.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    public static void main(String[] args) throws Exception, InterruptedException {
        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }

        String portString = args[0];
        int port = Integer.valueOf(portString);

        Server server = new Server(port);
        MongoModule mongoModule = MongoModule.getInstanse();

        AccountService accountService = new AccountService();

        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);

        RegistrationService registrationService = new RegistrationService(accountService);
        context.addServlet(new ServletHolder(registrationService),"/user");

        AuthorizationService authorizationService = new AuthorizationService(accountService);
        context.addServlet(new ServletHolder(authorizationService), "/session");

        ScoreboardService scoreboardService = new ScoreboardService();
        context.addServlet(new ServletHolder(scoreboardService), "/scoreboard");

        UserService userService = new UserService(accountService);
        context.addServlet(new ServletHolder(userService), "/user/*");

        server.setHandler(context);
        server.start();
        server.join();
    }
}

// curl -H "Content-Type: application/json" -X POST -d '{"login":"xyz","password":"xyz"}' http://localhost:8080/session
// curl -H "Content-Type: application/json" -X POST -b cookies.txt -d '{"login":"xyz","password":"xyz"}' http://localhost:8080/session
// curl -H "Content-Type: application/json" -X POST -c cookies.txt -d '{"email":"xyz", "login": "xyz","password":"xyz"}' http://localhost:8080/user
// curl -X DELETE "http://localhost:8080/user/56d20a7c92b9e55ff8503002"
// curl -i -H "Accept: application/json" -H "Content-Type: application/json" http://localhost:8080/scoreboard
// curl -i -H "Accept: application/json" -H "Content-Type: application/json" -b cookies.txt http://localhost:8080/user/56d20e2392b9e560b1514a15


