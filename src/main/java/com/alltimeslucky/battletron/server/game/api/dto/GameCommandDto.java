package com.alltimeslucky.battletron.server.game.api.dto;

public class GameCommandDto {

    private String commandString;

    public String getCommandString() {
        return commandString;
    }

    public void setCommandString(String commandString) {
        this.commandString = commandString;
    }

    @Override
    public String toString() {
        return "GameCommandDto{"
                + "commandString='" + commandString + '\''
                + '}';
    }
}
