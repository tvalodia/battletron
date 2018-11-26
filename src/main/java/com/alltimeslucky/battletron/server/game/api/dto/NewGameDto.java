package com.alltimeslucky.battletron.server.game.api.dto;

public class NewGameDto {

    private String playerId;
    private String playerOneType;
    private String playerTwoType;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayerOneType() {
        return playerOneType;
    }

    public void setPlayerOneType(String playerOneType) {
        this.playerOneType = playerOneType;
    }

    public String getPlayerTwoType() {
        return playerTwoType;
    }

    public void setPlayerTwoType(String playerTwoType) {
        this.playerTwoType = playerTwoType;
    }
}
