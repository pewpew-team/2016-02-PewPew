package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.AccountService;
import com.pewpew.pewpew.servlet.*;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

public class Main {
    @SuppressWarnings("DuplicateThrows")
    public static void main(String[] args) throws Exception, InterruptedException {
        if (args.length != 1) {
            System.out.append("Use port as the first argument");
            System.exit(1);
        }

        String portString = args[0];
        int port = Integer.valueOf(portString);

        Server server = new Server(port);

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


