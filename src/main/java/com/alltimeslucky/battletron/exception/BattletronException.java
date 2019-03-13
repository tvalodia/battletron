package com.alltimeslucky.battletron.exception;

import java.util.LinkedList;
import java.util.List;

import javax.ws.rs.ext.Provider;

@Provider
public class BattletronException extends Exception {

    private static final long serialVersionUID = 1L;
    private List<BattletronException> exceptions = new LinkedList<>();
    private String field;
    private ExceptionCode code;

    public BattletronException() {
        super("Unknown");
    }

    public BattletronException(String string) {
        super(string);
    }

    /**
     * Constructor.
     * @param code The specific ExceptionCode
     * @param cause The cause of this Exception
     */
    public BattletronException(ExceptionCode code, Throwable cause) {
        super(code.getMessage(), cause);
        this.code = code;
    }

    /**
     * Constructor.
     * @param code The specific ExceptionCode
     */
    public BattletronException(ExceptionCode code) {
        super(code.getMessage());
        this.code = code;
    }

    /**
     * Constructor.
     *
     * @param code The specific ExceptionCode
     * @param field The name of the field in violation
     */
    public BattletronException(ExceptionCode code, String field) {
        super(code.getMessage());
        this.code = code;
        this.field = field;
    }

    public void add(BattletronException exception) {
        exceptions.add(exception);
    }

    public int exceptionCount() {
        return exceptions.size();
    }

    public ExceptionCode getCode() {
        return code;
    }

    public String getField() {
        return field;
    }

    public List<BattletronException> getExceptions() {
        return exceptions;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append(code).append(" - ").append(getMessage()).append(" - ").append(field).append(System.lineSeparator());

        for (BattletronException cause : exceptions) {
            builder.append(cause).append(System.lineSeparator());
        }

        return builder.toString();
    }
}
