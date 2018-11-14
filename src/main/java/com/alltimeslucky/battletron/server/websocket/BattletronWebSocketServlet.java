package com.alltimeslucky.battletron.server.websocket;

import javax.inject.Inject;

import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class BattletronWebSocketServlet extends WebSocketServlet {

    private final ClientWebSocketRepository clientWebSocketRepository;
    private final WebSocketGameUpdateRouter webSocketGameUpdateRouter;

    @Inject
    public BattletronWebSocketServlet(ClientWebSocketRepository clientWebSocketRepository,
                                      WebSocketGameUpdateRouter webSocketGameUpdateRouter) {

        this.clientWebSocketRepository = clientWebSocketRepository;
        this.webSocketGameUpdateRouter = webSocketGameUpdateRouter;
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(ClientWebSocket.class);

        // Set your custom Creator
        webSocketServletFactory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
                return new ClientWebSocket(clientWebSocketRepository, webSocketGameUpdateRouter);
            }
        });
    }
}