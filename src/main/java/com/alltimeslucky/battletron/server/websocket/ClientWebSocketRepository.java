package com.alltimeslucky.battletron.server.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class ClientWebSocketRepository {

    private ConcurrentHashMap<String, ClientWebSocket> data;

    public ClientWebSocketRepository() {
        data = new ConcurrentHashMap<>();
    }

    public void add(String onlinePlayerId, ClientWebSocket socket) {
        data.put(onlinePlayerId, socket);
    }

    public ClientWebSocket get(String onlinePlayerId) {
        return data.get(onlinePlayerId);
    }

    public void delete(String onlinePlayerId) {
        data.remove(onlinePlayerId);
    }
}
