package com.alltimeslucky.battletron.server.websocket;

public class WebSocketGameStateListenerFactory {

    private static WebSocketGameStateListener listener;

    public static WebSocketGameStateListener getWebSocketGameStateListener() {
        if (listener == null) {
            listener = new WebSocketGameStateListener();
        }

        return listener;
    }
}
