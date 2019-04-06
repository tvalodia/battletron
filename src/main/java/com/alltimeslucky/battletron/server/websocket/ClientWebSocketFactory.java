package com.alltimeslucky.battletron.server.websocket;

import org.springframework.stereotype.Component;
import org.springframework.web.socket.WebSocketSession;

@Component
public class ClientWebSocketFactory {

    public ClientWebSocket get(WebSocketSession webSocketSession) {
        return new ClientWebSocket(webSocketSession);
    }
}
