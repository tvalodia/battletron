package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;

public class PlayerControllerSettings {

    private PlayerControllerType playerControllerType;
    private ClientWebSocket clientWebSocket;
    private String remoteAiHost;

    public PlayerControllerSettings(PlayerControllerType playerControllerType) {
        this.playerControllerType = playerControllerType;
    }

    public PlayerControllerType getPlayerControllerType() {
        return playerControllerType;
    }

    public void setPlayerControllerType(PlayerControllerType playerControllerType) {
        this.playerControllerType = playerControllerType;
    }

    public ClientWebSocket getClientWebSocket() {
        return clientWebSocket;
    }

    public void setClientWebSocket(ClientWebSocket clientWebSocket) {
        this.clientWebSocket = clientWebSocket;
    }

    public String getRemoteAiHost() {
        return remoteAiHost;
    }

    public void setAiRemoteHost(String remoteAiHost) {
        this.remoteAiHost = remoteAiHost;
    }
}
