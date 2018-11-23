package com.alltimeslucky.battletron.server.websocket;

import java.util.concurrent.ConcurrentHashMap;

public class ClientWebSocketRepository {

    private ConcurrentHashMap<String, ClientWebSocket> data;

    public ClientWebSocketRepository() {
        data = new ConcurrentHashMap<>();
    }

    public void add(String sessionId, ClientWebSocket socket) {
        data.put(sessionId, socket);
    }

    public ClientWebSocket get(String sessionId) {
        return data.get(sessionId);
    }

    public void delete(String sessionId) {
        data.remove(sessionId);
    }

    /**
     * Indicates whether a ClientWebSocket with the specified sessiondId exists in this repository.
     *
     * @param sessionId The sessionId of the web socket
     * @return True if the ClientWebSocket exists, otherwise returns false
     */
    public boolean contains(String sessionId) {
        if (sessionId == null) {
            return false;
        } else {
            return data.containsKey(sessionId);
        }
    }
}
