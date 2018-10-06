package com.alltimeslucky.battletron.server.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class EventServlet extends WebSocketServlet
{
    protected static final Logger LOG = LogManager.getLogger();

    private String constructorArgument;

    public EventServlet(String constructorArgument) {
        this.constructorArgument = constructorArgument;
        LOG.debug("EventServlet - " + constructorArgument);

    }
    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory)
    {
        webSocketServletFactory.register(EventSocket.class);

        // Get the current creator (for reuse)
        final WebSocketCreator creator = webSocketServletFactory.getCreator();

        // Set your custom Creator
        webSocketServletFactory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
                EventSocket webSocket = (EventSocket) creator.createWebSocket(servletUpgradeRequest, servletUpgradeResponse);

                // Use the object created by the default creator and inject your members
                webSocket.setArgumentProperty(constructorArgument);

                return webSocket;
            }
        });
    }
}