package com.alltimeslucky.battletron.exception;

public enum ExceptionCode {

    ALREADY_JOINED_GAME("You're already in this game"),
    VALIDATION("Validation errors"),
    NOT_FOUND("No record found"),
    MISSING_VALUE("Mandatory value has not been specified"),
    INVALID_VALUE("Specified value is invalid"),
    GAME_ALREADY_FULL("This game is already full"),
    REMOTE_AI_CONNECTION_ERROR("Unable to connect to the remote AI server"),
    UNABLE_TO_PAUSE("Unable to pause the game");


    private String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
