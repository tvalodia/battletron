package com.alltimeslucky.battletron.server.game.api.dto;

public class NewGameDto {

    private String sessionId;
    private NewGamePlayerDto playerOne;
    private NewGamePlayerDto playerTwo;

    public String getSessionId() {
        return sessionId;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public NewGamePlayerDto getPlayerOne() {
        return playerOne;
    }

    public void setPlayerOne(NewGamePlayerDto playerOne) {
        this.playerOne = playerOne;
    }

    public NewGamePlayerDto getPlayerTwo() {
        return playerTwo;
    }

    public void setPlayerTwo(NewGamePlayerDto playerTwo) {
        this.playerTwo = playerTwo;
    }
}
