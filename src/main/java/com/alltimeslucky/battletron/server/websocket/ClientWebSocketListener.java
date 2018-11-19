package com.alltimeslucky.battletron.server.websocket;

public interface ClientWebSocketListener {

    void onConnect(ClientWebSocket clientWebSocket);
    void onDisconnect(ClientWebSocket clientWebSocket);

}
