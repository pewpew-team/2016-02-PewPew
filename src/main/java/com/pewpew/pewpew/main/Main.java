package com.pewpew.pewpew.main;

import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.model.AccountService;
import com.pewpew.pewpew.servlet.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.HandlerList;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class Main {
    @SuppressWarnings("all")
    static final Logger logger = LoggerFactory.getLogger(Main.class);
    public static void main(String[] args) throws Exception {

        int port = Settings.DEFAULT_PORT;
        Path pathToStatic = Paths.get(Settings.DEFAULT_STATIC_PATH);

        //noinspection OverlyBroadCatchBlock
        try {
            for (String arg : args) {
                String argName = arg.split("=")[0];
                String argValue = arg.split("=")[1];
                switch (argName) {
                    case "--port": {
                        port = Integer.valueOf(argValue);
                        break;
                    }
                    case "--static": {
                        pathToStatic = Paths.get(argValue);
                        if(!Files.isDirectory(pathToStatic)) throw new IllegalArgumentException();
                        break;
                    }
                    default: {
                        throw new IllegalArgumentException(argName);
                    }
                }
            }
        }
        catch (IllegalArgumentException ex) {
            logger.error(String.format("syntax or parameter error: %s \n %s\n", ex.getMessage(),
                    "Usage:$ java -jar <jar file> [--port=<port>] [--static=</path/to/static>]"));
            System.exit(1);
        }

        logger.info(String.format("Starting at port: %d \n", port));
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


