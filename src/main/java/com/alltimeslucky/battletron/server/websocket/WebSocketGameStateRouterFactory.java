package com.alltimeslucky.battletron.server.websocket;


/**
 * This class provides an instance of the WebSocketGameStateRouter.
 */
public class WebSocketGameStateRouterFactory {

    private static WebSocketGameStateRouter instance;

    /**
     * Constructor.
     * @return The instance of WebSocketGameStateRouter
     */
    public static WebSocketGameStateRouter getWebSocketGameStateUpdateRouter() {
        if (instance == null) {
            instance = new WebSocketGameStateRouter();
        }
        return instance;
    }


}
