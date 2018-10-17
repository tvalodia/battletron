package com.alltimeslucky.battletron.server.websocket;

/**
 * This factory returns the WebSocketGameStateListener instance.
 */
public class WebSocketGameStateListenerFactory {

    private static WebSocketGameStateListener listener;

    /**
     * Always returns the same instance of a WebSocketGameStateListener.
     * @return The WebSocketGameStateListener instance
     */
    public static WebSocketGameStateListener getWebSocketGameStateListener() {
        if (listener == null) {
            listener = new WebSocketGameStateListener();
        }

        return listener;
    }
}
