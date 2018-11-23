package com.alltimeslucky.battletron.exception;

public enum ExceptionCode {

    VALIDATION("Validation errors"),
    NOT_FOUND("No record found"),
    MISSING_VALUE("Mandatory value has not been specified"),
    INVALID_VALUE("Specified value is invalid");

    private String message;

    ExceptionCode(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
