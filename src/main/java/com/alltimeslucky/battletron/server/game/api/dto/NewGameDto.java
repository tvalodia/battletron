package com.alltimeslucky.battletron.server.game.api.dto;

public class NewGameDto {

    private String playerId;
    private String player1Type;
    private String player2Type;

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getPlayer1Type() {
        return player1Type;
    }

    public void setPlayer1Type(String player1Type) {
        this.player1Type = player1Type;
    }

    public String getPlayer2Type() {
        return player2Type;
    }

    public void setPlayer2Type(String player2Type) {
        this.player2Type = player2Type;
    }
}
