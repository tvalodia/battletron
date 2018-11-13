package com.alltimeslucky.battletron.server;

import com.alltimeslucky.battletron.server.api.game.GameApi;
import com.alltimeslucky.battletron.server.websocket.BattletronWebSocketServlet;

import java.util.EnumSet;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;

import javax.servlet.DispatcherType;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.rewrite.handler.RewriteHandler;
import org.eclipse.jetty.rewrite.handler.RewriteRegexRule;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.DefaultServlet;
import org.eclipse.jetty.servlet.FilterHolder;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;
import org.eclipse.jetty.servlets.CrossOriginFilter;
import org.eclipse.jetty.webapp.WebAppContext;

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

        Server jettyServer = new Server(PORT);

        // Create the web app mainWebAppContext
        WebAppContext mainWebAppContext = new WebAppContext();
        mainWebAppContext.setContextPath("/");
        mainWebAppContext.setWelcomeFiles(new String[]{"index.html"});
        mainWebAppContext.setResourceBase(getWebappWebRootUri());

        //Set up the API servlet
        ServletHolder jerseyServlet = mainWebAppContext.addServlet(
                org.glassfish.jersey.servlet.ServletContainer.class, "/api/*");
        jerseyServlet.setInitOrder(0);

        jerseyServlet.setInitParameter("jersey.config.server.provider.classnames", GameApi.class.getCanonicalName());

        RewriteHandler urlRewriteHandler = new Html5PushStateConditionalRewriteHandler(mainWebAppContext);
        urlRewriteHandler.setRewriteRequestURI(true);
        urlRewriteHandler.setRewritePathInfo(false);
        urlRewriteHandler.setOriginalPathAttribute("requestedPath");

        RewriteRegexRule html5pushState = new RewriteRegexRule();
        html5pushState.setRegex("/.*");
        html5pushState.setReplacement("/index.html");
        urlRewriteHandler.addRule(html5pushState);

        // Handler Structure: UrlRewriteHandler will filter URLs before they reach the webapp context.
        urlRewriteHandler.setHandler(mainWebAppContext);
        jettyServer.setHandler(urlRewriteHandler);


        // Add a websocket to a specific path spec
        //Use this when not injecting a dependency
        ServletHolder holderEvents = new ServletHolder("ws-player", BattletronWebSocketServlet.class);
        mainWebAppContext.addServlet(holderEvents, "/player/*");

        //Disabling the CORS security feature so that the Angular webapp hosted on the NodeJS live DEV server is allowed to
        //communicate with this Jetty server.
        FilterHolder cors = mainWebAppContext.addFilter(CrossOriginFilter.class, "/*", EnumSet.of(DispatcherType.REQUEST));
        cors.setInitParameter(CrossOriginFilter.ALLOWED_ORIGINS_PARAM, "*");
        cors.setInitParameter(CrossOriginFilter.ACCESS_CONTROL_ALLOW_ORIGIN_HEADER, "*");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_METHODS_PARAM, "GET,POST,HEAD");
        cors.setInitParameter(CrossOriginFilter.ALLOWED_HEADERS_PARAM, "X-Requested-With,Content-Type,Accept,Origin");


        try {
            jettyServer.start();
            jettyServer.join();
        } finally {
            jettyServer.destroy();
        }
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

