package com.pewpew.pewpew.main;

import com.mongodb.MongoException;
import com.mongodb.MongoSocketOpenException;
import com.pewpew.pewpew.common.Settings;
import com.pewpew.pewpew.mechanics.GameMechanics;
import com.pewpew.pewpew.mechanics.GameMechanicsImpl;
import com.pewpew.pewpew.rest.ScoreboardService;
import com.pewpew.pewpew.rest.SessionService;
import com.pewpew.pewpew.rest.UserService;
import com.pewpew.pewpew.websoket.*;
import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.ServerConnector;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.hk2.utilities.binding.AbstractBinder;
import org.glassfish.jersey.server.ResourceConfig;
import org.glassfish.jersey.servlet.ServletContainer;

import java.io.FileInputStream;
import java.io.IOException;
import java.net.BindException;
import java.net.ConnectException;
import java.net.UnknownHostException;
import java.util.Properties;


public class Main {
    @SuppressWarnings("OverlyBroadThrowsClause")
    public static void main(String[] args) throws Exception {
        int port = -1;
        String staticPath = "";
        if (args.length == 2) {
            port = Integer.valueOf(args[0]);
            staticPath = String.valueOf(args[1]);
        } else {
            System.err.println("Specify port");
            System.exit(1);
        }

        final Server server = new Server();
        final ServerConnector connector = new ServerConnector(server);
        final Properties property = new Properties();
        try(FileInputStream fileInputStream =
                    new FileInputStream("src/main/java/com/pewpew/pewpew/resources/server.properties")) {
            property.load(fileInputStream);
        } catch (IOException e) {
            System.out.println("Can't start mongo");
        }
        connector.setHost(property.getProperty("server.host"));
        connector.setPort(port);
        server.addConnector(connector);

        final ServletContextHandler contextHandler = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        final Context context = new Context();
        try {
            final AccountService accountService = new AccountServiceImpl();
            context.put(AccountService.class, accountService);
            final ResourceConfig config = new ResourceConfig(SessionService.class,
                    UserService.class, ScoreboardService.class, GsonMessageBodyHandler.class);
            config.register(new AbstractBinder() {
                @Override
                protected void configure() {
                    bind(context);
                }
            });

            final ServletHolder servletHolder = new ServletHolder(new ServletContainer(config));

            contextHandler.addServlet(servletHolder, "/*");

            final WebSocketService webSocketService = new WebSocketServiceImpl();
            final GameMechanics gameMechanics = new GameMechanicsImpl(webSocketService);
            contextHandler.addServlet(new ServletHolder(new GameSocketServelet(
                    webSocketService, gameMechanics)), "/ws");


            final ResourceHandler resourceHandler = new ResourceHandler();
            resourceHandler.setDirectoriesListed(true);
            resourceHandler.setResourceBase(staticPath);

            final HandlerCollection handlerCollection = new HandlerCollection();
            handlerCollection.setHandlers(new Handler[]{resourceHandler,
                    contextHandler, new DefaultHandler()});

            server.setHandler(handlerCollection);

            server.start();
            gameMechanics.run();
        } catch (MongoException e) {
            e.printStackTrace();
            System.exit(1);
        }
    }
}


