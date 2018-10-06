package com.alltimeslucky.battletron.server.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EventSocket extends WebSocketAdapter {

    private static final Logger LOG = LogManager.getLogger();

    private WebSocketGameStateListener listener;

    public EventSocket() {
        listener = WebSocketGameStateListenerFactory.getWebSocketGameStateListener();
    }

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        LOG.debug("New WebSocket connection: " + getSession());
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        LOG.debug("Game " + message + " update stream requested from " + getSession());
        listener.registerSession(getSession(), Long.valueOf(message));
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
        listener.unregisterSession(getSession());
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        listener.unregisterSession(getSession());
        cause.printStackTrace(System.err);

    }

    public void setListener(WebSocketGameStateListener listener) {
        this.listener = listener;
    }
}
