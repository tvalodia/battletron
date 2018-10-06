package com.alltimeslucky.battletron.server.websocket;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WebSocketAdapter;

public class EventSocket extends WebSocketAdapter {

    protected static final Logger LOG = LogManager.getLogger();

    private String argumentProperty;

    @Override
    public void onWebSocketConnect(Session sess) {
        super.onWebSocketConnect(sess);
        System.out.println("Socket Connected: " + sess);
    }

    @Override
    public void onWebSocketText(String message) {
        super.onWebSocketText(message);
        System.out.println("Received TEXT message: " + message + " - " + argumentProperty );
    }

    @Override
    public void onWebSocketClose(int statusCode, String reason) {
        super.onWebSocketClose(statusCode, reason);
        System.out.println("Socket Closed: [" + statusCode + "] " + reason);
    }

    @Override
    public void onWebSocketError(Throwable cause) {
        super.onWebSocketError(cause);
        cause.printStackTrace(System.err);
    }

    public String getArgumentProperty() {
        return argumentProperty;
    }

    public void setArgumentProperty(String argumentProperty) {
        LOG.debug(argumentProperty);
        this.argumentProperty = argumentProperty;
    }
}
