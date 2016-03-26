package com.pewpew.pewpew.main;

import org.eclipse.jetty.server.Handler;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.DefaultHandler;
import org.eclipse.jetty.server.handler.HandlerCollection;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;


public class Main {
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

        Server server = new Server(port);

        final ServletContextHandler context = new ServletContextHandler(server, "/", ServletContextHandler.SESSIONS);

        final ServletHolder servletHolder = new ServletHolder(ServletContainer.class);
        servletHolder.setInitParameter("javax.ws.rs.Application",
                "com.pewpew.pewpew.main.RestApplication");

        ResourceHandler resourceHandler = new ResourceHandler();
        resourceHandler.setDirectoriesListed(true);
        resourceHandler.setResourceBase(staticPath);

        context.addServlet(servletHolder, "/*");

        HandlerCollection handlerCollection = new HandlerCollection();
        handlerCollection.setHandlers(new Handler[] { resourceHandler,
                context, new DefaultHandler() });


        server.setHandler(handlerCollection);
        server.start();
        server.join();
    }
}


