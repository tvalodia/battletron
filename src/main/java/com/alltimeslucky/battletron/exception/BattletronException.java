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
}
