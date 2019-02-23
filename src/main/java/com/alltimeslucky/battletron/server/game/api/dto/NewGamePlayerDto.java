package com.alltimeslucky.battletron.server.game.api.dto;

import java.util.Map;

public class NewGamePlayerDto {

    private String playerType;
    private String aiRemoteHost;

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public String getAiRemoteHost() {
        return aiRemoteHost;
    }

    public void setAiRemoteHost(String aiRemoteHost) {
        this.aiRemoteHost = aiRemoteHost;
    }

}
