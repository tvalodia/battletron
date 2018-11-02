package com.alltimeslucky.battletron.server.websocket;


/**
 * This class provides an instance of the WebSocketGameUpdateRouter.
 */
public class WebSocketGameStateRouterFactory {

    private static WebSocketGameUpdateRouter instance;

    /**
     * Constructor.
     * @return The instance of WebSocketGameUpdateRouter
     */
    public static WebSocketGameUpdateRouter getWebSocketGameStateUpdateRouter() {
        if (instance == null) {
            instance = new WebSocketGameUpdateRouter();
        }
        return instance;
    }


}
