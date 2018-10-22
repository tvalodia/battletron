package com.alltimeslucky.battletron.server;

import com.alltimeslucky.battletron.server.api.game.GameApi;
import com.alltimeslucky.battletron.server.websocket.BattletronWebSocketServlet;

import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;


//import org.apache.logging.log4j.L;

/**
 * A Jetty-based application to run simulations on a headless server.
 */
public class BattletronServer {

    protected static final Logger LOG = LogManager.getLogger();
    private static final int PORT = 8080;

    /**
     * Server entry point.
     *
     * @param args Program arguments
     * @throws Exception Thrown when an error has occurred.
     */
    public static void main(String[] args) throws Exception {
        LOG.info("Starting web server on port " + PORT);
        ServletContextHandler context = new ServletContextHandler(ServletContextHandler.SESSIONS);
        context.setContextPath("/");

        Server jettyServer = new Server(PORT);
        jettyServer.setHandler(context);

        //Set up the API servlet
        ServletHolder jerseyServlet = context.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", GameApi.class.getCanonicalName());

        DefaultServlet defaultServlet = new DefaultServlet();
        ServletHolder holderPwd = new ServletHolder("default-static", defaultServlet);
        holderPwd.setInitParameter("resourceBase", getStaticWebRootUri());
        holderPwd.setInitParameter("dirAllowed","true");
        holderPwd.setInitParameter("pathInfoOnly","true");
        context.addServlet(holderPwd, "/*");

        DefaultServlet webappDefaultServlet = new DefaultServlet();
        ServletHolder webappServletHolder = new ServletHolder("default-webapp", webappDefaultServlet);
        webappServletHolder.setInitParameter("resourceBase", getWebappWebRootUri());
        webappServletHolder.setInitParameter("dirAllowed","true");
        webappServletHolder.setInitParameter("pathInfoOnly","true");
        context.addServlet(webappServletHolder, "/webapp/*");

        // Add a websocket to a specific path spec
        //Use this when not injecting a dependency
        ServletHolder holderEvents = new ServletHolder("ws-player", BattletronWebSocketServlet.class);
        context.addServlet(holderEvents, "/player/*");

        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
    }

    private static String getStaticWebRootUri() throws URISyntaxException {
        //Set up the Resource (static file hosting) servlet
        // Figure out what path to serve content from
        ClassLoader cl = BattletronServer.class.getClassLoader();
        // We look for a file, as ClassLoader.getResource() is not
        // designed to look for directories (we resolve the directory later)
        URL f = cl.getResource("html/index.html");
        if (f == null) {
            throw new RuntimeException("Unable to find resource directory");
        }

        // Resolve file to directory
        URI webRootUri = f.toURI().resolve("./").normalize();
        LOG.info("Static WebRoot is " + webRootUri);
        return webRootUri.toString();
    }

    private static String getWebappWebRootUri() throws URISyntaxException {
        ClassLoader cl = BattletronServer.class.getClassLoader();
        URL f = cl.getResource("webapp/index.html");
        if (f == null) {
            throw new RuntimeException("Unable to find resource directory");
        }

        // Resolve file to directory
        URI webRootUri = f.toURI().resolve("./").normalize();
        LOG.info("Webapp WebRoot is " + webRootUri);
        return webRootUri.toString();
    }
}

