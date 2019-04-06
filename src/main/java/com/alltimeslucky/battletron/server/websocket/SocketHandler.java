package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.server.session.repository.SessionRepository;
import com.alltimeslucky.battletron.server.session.service.Session;
import com.alltimeslucky.battletron.server.session.service.SessionFactory;

import java.io.IOException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.CloseStatus;
import org.springframework.web.socket.TextMessage;
import org.springframework.web.socket.WebSocketSession;
import org.springframework.web.socket.handler.TextWebSocketHandler;

@Component
public class SocketHandler extends TextWebSocketHandler {

    private static final Logger LOG = LogManager.getLogger();

    private final SessionFactory sessionFactory;
    private final SessionRepository sessionRepository;
    private final ClientWebSocketController clientWebSocketController;
    private final ClientWebSocketFactory clientWebSocketFactory;

    /**
     * Constructor.
     * @param sessionFactory Used to create new sessions when clients connect.
     * @param sessionRepository Used to store sessions.
     * @param clientWebSocketFactory Used to create new instances of ClientWebSockets to represent client sockets.
     * @param clientWebSocketController Used to register new ClientWebSockets for game updates.
     */
    public SocketHandler(SessionFactory sessionFactory, SessionRepository sessionRepository, ClientWebSocketFactory clientWebSocketFactory,
                         ClientWebSocketController clientWebSocketController) {

        this.sessionFactory = sessionFactory;
        this.sessionRepository = sessionRepository;
        this.clientWebSocketFactory = clientWebSocketFactory;
        this.clientWebSocketController = clientWebSocketController;
    }

    @Override
    public void handleTextMessage(WebSocketSession webSocketSession, TextMessage message) {
        sessionRepository.get(webSocketSession.getId()).getClientWebSocket().onWebSocketText(message.getPayload());
    }

    @Override
    public void afterConnectionEstablished(WebSocketSession webSocketSession) throws Exception {
        String sessionId = webSocketSession.getId();
        ClientWebSocket clientWebSocket = clientWebSocketFactory.get(webSocketSession);
        Session session = sessionFactory.get(sessionId, clientWebSocket);
        sessionRepository.add(session.getId(), session);

        LOG.debug(String.format("New WebSocket connection: %s ", session.getId()));

        try {
            webSocketSession.sendMessage(new TextMessage("id=" + sessionId));
        } catch (IOException e) {
            LOG.error(e.getMessage());
            webSocketSession.close(CloseStatus.SERVER_ERROR);
        }
    }

    @Override
    public void afterConnectionClosed(WebSocketSession session, CloseStatus status) {
        LOG.debug(String.format("WebSocket connection %s closed with status %s ", session.getId(), status));
        clientWebSocketController.deregisterForUpdates(session.getId());
        sessionRepository.delete(session.getId());

    }
}
