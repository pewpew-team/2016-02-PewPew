package com.pewpew.pewpew.main;

import com.pewpew.pewpew.model.AccountService;
import com.pewpew.pewpew.servlet.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    public static void main(String[] args) throws Exception {
        if (args.length != 2) {
            System.out.println("Usage: java -jar <jar file> <port> </path/to/static>");
            System.exit(1);
        }

        int port = 0;
        Path pathToStatic = null;

        //noinspection OverlyBroadCatchBlock
        try {
            port = Integer.valueOf(args[0]);
            pathToStatic = Paths.get(args[1]);
            if(!Files.isDirectory(pathToStatic)) throw new IllegalArgumentException();
        }
        catch (IllegalArgumentException ex) {
            System.out.println("Error: please input valid format of port and path.");
            System.exit(1);
        }

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

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase(pathToStatic.toString());

        HandlerList handlers = new HandlerList();
        handlers.setHandlers(new Handler[]{resourceHandler, context});
        server.setHandler(handlers);

        server.start();
        server.join();
    }
}


