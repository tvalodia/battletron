package com.alltimeslucky.battletron.server.session;

import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;

public class Session {

    private String id;
    private PlayerController playerController;
    private Long gameId;
    private ClientWebSocket clientWebSocket;

    public Session(String id) {
        this.id = id;
    }

    public PlayerController getPlayerController() {
        return playerController;
    }

    public void setPlayerController(PlayerController playerController) {
        this.playerController = playerController;
    }

    public Long getGameId() {
        return gameId;
    }

    public void setGameId(Long gameId) {
        this.gameId = gameId;
    }

    public ClientWebSocket getClientWebSocket() {
        return clientWebSocket;
    }

    public void setClientWebSocket(ClientWebSocket clientWebSocket) {
        this.clientWebSocket = clientWebSocket;
    }
}
