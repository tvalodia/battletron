package com.alltimeslucky.battletron.server.game.api.dto;

public class NewGameDto {

    private String playerId;
    private String playerOneType;
    private String player2Type;

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

    public String getPlayer2Type() {
        return player2Type;
    }

    public void setPlayer2Type(String player2Type) {
        this.player2Type = player2Type;
    }
}
