package com.alltimeslucky.battletron.server.exceptionmapper;

import java.util.LinkedList;
import java.util.List;

/**
 * This class is used as a transfer object for returning errors to web clients.
 */
public class ExceptionDto {

    private String code;
    private String description;
    private List<ExceptionDto> causes = new LinkedList<>();
    private String field;

    /**
     * Constructor.
     *
     * @param code The error code
     * @param description The description of the error
     * @param field The name of the field that caused the error/
     */
    public ExceptionDto(String code, String description, String field) {
        this.code = code;
        this.description = description;
        this.field = field;
    }

    public void add(ExceptionDto exceptionDto) {
        causes.add(exceptionDto);
    }

    public String getField() {
        return field;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public List<ExceptionDto> getCauses() {
        return causes;
    }
}
