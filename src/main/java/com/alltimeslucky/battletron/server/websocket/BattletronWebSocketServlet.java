package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.server.game.service.GameService;

import javax.inject.Inject;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;
import org.eclipse.jetty.websocket.servlet.WebSocketCreator;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

@SuppressWarnings("serial")
public class BattletronWebSocketServlet extends WebSocketServlet {

    private static final Logger LOG = LogManager.getLogger();

    private final ClientWebSocketRepository clientWebSocketRepository;
    private final ClientWebSocketController clientWebSocketController;
    private GameService gameService;

    private ClientWebSocketListener clientWebSocketListener = new ClientWebSocketListener() {
        @Override
        public void onConnect(ClientWebSocket clientWebSocket) {
            clientWebSocketRepository.add(clientWebSocket.getSessionId(), clientWebSocket);
        }

        @Override
        public void onDisconnect(ClientWebSocket clientWebSocket) {
            clientWebSocketRepository.delete(clientWebSocket.getSessionId());
            clientWebSocketController.deregisterForUpdates(clientWebSocket.getSessionId());
        }
    };

    /**
     * Constructor.
     *
     * @param clientWebSocketRepository The instance of ClientWebSocketRepository
     * @param clientWebSocketController ClientWebSocketController clientWebSocketController
     */
    @Inject
    public BattletronWebSocketServlet(ClientWebSocketRepository clientWebSocketRepository,
                                      ClientWebSocketController clientWebSocketController,
                                      GameService gameService) {

        this.clientWebSocketRepository = clientWebSocketRepository;
        this.clientWebSocketController = clientWebSocketController;
        this.gameService = gameService;
    }

    @Override
    public void configure(WebSocketServletFactory webSocketServletFactory) {
        webSocketServletFactory.register(ClientWebSocket.class);

        // Set your custom Creator
        webSocketServletFactory.setCreator(new WebSocketCreator() {
            @Override
            public Object createWebSocket(ServletUpgradeRequest servletUpgradeRequest, ServletUpgradeResponse servletUpgradeResponse) {
                ClientWebSocket webSocket = new ClientWebSocket();
                webSocket.setListener(clientWebSocketListener);
                return webSocket;
            }
        });
    }
}