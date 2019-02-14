package com.alltimeslucky.battletron.exception;

public enum ExceptionCode {

    ALREADY_JOINED_GAME("You're already in this game"),
    VALIDATION("Validation errors"),
    NOT_FOUND("No record found"),
    MISSING_VALUE("Mandatory value has not been specified"),
    INVALID_VALUE("Specified value is invalid"),
    GAME_ALREADY_FULL("This game is already full");

    private String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
