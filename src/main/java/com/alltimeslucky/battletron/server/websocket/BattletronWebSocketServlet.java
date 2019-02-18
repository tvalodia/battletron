package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.server.game.service.GameService;
import com.alltimeslucky.battletron.server.session.Session;
import com.alltimeslucky.battletron.server.session.SessionFactory;
import com.alltimeslucky.battletron.server.session.SessionRepository;

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

    private final SessionRepository sessionRepository;
    private final SessionFactory sessionFactory;
    private final ClientWebSocketController clientWebSocketController;

    private ClientWebSocketListener clientWebSocketListener = new ClientWebSocketListener() {
        @Override
        public void onConnect(ClientWebSocket clientWebSocket) {
            Session session = sessionFactory.get(clientWebSocket.getSessionId());
            session.setClientWebSocket(clientWebSocket);
            sessionRepository.add(clientWebSocket.getSessionId(), session);
        }

        @Override
        public void onDisconnect(ClientWebSocket clientWebSocket) {
            sessionRepository.delete(clientWebSocket.getSessionId());
            clientWebSocketController.deregisterForUpdates(clientWebSocket.getSessionId());
        }
    };

    /**
     * Constructor.
     *
     * @param sessionRepository         The instance of ClientWebSocketRepository
     * @param clientWebSocketController ClientWebSocketController clientWebSocketController
     */
    @Inject
    public BattletronWebSocketServlet(SessionFactory sessionFactory,
                                      SessionRepository sessionRepository,
                                      ClientWebSocketController clientWebSocketController) {
        this.sessionFactory = sessionFactory;
        this.sessionRepository = sessionRepository;
        this.clientWebSocketController = clientWebSocketController;
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