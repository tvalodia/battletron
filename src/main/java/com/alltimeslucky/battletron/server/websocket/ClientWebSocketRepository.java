package com.alltimeslucky.battletron.server.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class ClientWebSocketRepository {

    private static ClientWebSocketRepository instance;
    private ConcurrentHashMap<String, ClientWebSocket> data;


    private ClientWebSocketRepository() {
        data = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance of the ClientWebSocketRepository.
     *
     * @return The instance of ClientWebSocketRepository
     */
    public static ClientWebSocketRepository getInstance() {
        if (instance == null) {
            instance = new ClientWebSocketRepository();
        }
        return instance;
    }

    public void addOnlinePlayerSocket(String onlinePlayerId, ClientWebSocket socket) {
        data.put(onlinePlayerId, socket);
    }

    public ClientWebSocket get(String onlinePlayerId) {
        return data.get(onlinePlayerId);
    }

    public void delete(String onlinePlayerId) {
        data.remove(onlinePlayerId);
    }
}
