package com.alltimeslucky.battletron.server.session.service;

import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;

import org.springframework.stereotype.Component;

@Component
public class SessionFactory {

    public Session get(String sessionId) {
        return new Session(sessionId);
    }

    /**
     * Creates a new Session.
     * @param sessionId The session id to use for the new Session.
     * @param clientWebSocket The ClientWebSocket to associate with the new session.
     * @return A new session initialised with the provide parameters.
     */
    public Session get(String sessionId, ClientWebSocket clientWebSocket) {
        Session session = new Session(sessionId);
        session.setClientWebSocket(clientWebSocket);
        return session;
    }
}
