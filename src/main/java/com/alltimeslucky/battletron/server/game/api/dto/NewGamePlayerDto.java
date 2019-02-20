package com.alltimeslucky.battletron.server.game.api.dto;

import java.util.Map;

public class NewGamePlayerDto {

    private String playerType;
    private Map<String, String> parameters;

    public String getPlayerType() {
        return playerType;
    }

    public void setPlayerType(String playerType) {
        this.playerType = playerType;
    }

    public Map<String, String> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, String> parameters) {
        this.parameters = parameters;
    }
}
