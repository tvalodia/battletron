package com.alltimeslucky.battletron.server.websocket;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

public class OnlinePlayerSocketRepository {

    private static OnlinePlayerSocketRepository instance;
    private ConcurrentHashMap<String, OnlinePlayerWebSocket> data;


    private OnlinePlayerSocketRepository() {
        data = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance of the OnlinePlayerSocketRepository.
     *
     * @return The instance of OnlinePlayerSocketRepository
     */
    public static OnlinePlayerSocketRepository getInstance() {
        if (instance == null) {
            instance = new OnlinePlayerSocketRepository();
        }
        return instance;
    }

    public void addOnlinePlayerSocket(String onlinePlayerId, OnlinePlayerWebSocket socket) {
        data.put(onlinePlayerId, socket);
    }

    public OnlinePlayerWebSocket getOnlinePlayerSocket(String onlinePlayerId) {
        return data.get(onlinePlayerId);
    }

    public Collection<OnlinePlayerWebSocket> getAllOnlinePlayerSockets() {
        return data.values();
    }

    public void delete(String onlinePlayerId) {
        data.remove(onlinePlayerId);
    }
}
