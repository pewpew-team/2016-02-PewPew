package com.pewpew.pewpew.main;

import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.server.handler.ResourceHandler;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.glassfish.jersey.servlet.ServletContainer;


public class Main {
    public static void main(String[] args) throws Exception {
        int port = -1;
        if (args.length == 1) {
            port = Integer.valueOf(args[0]);
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
        resourceHandler.setResourceBase("static");

        context.addServlet(servletHolder, "/*");
        context.setHandler(resourceHandler);

        server.start();
        server.join();
    }
}


