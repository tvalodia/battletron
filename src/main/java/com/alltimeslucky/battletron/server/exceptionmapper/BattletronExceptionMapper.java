package com.alltimeslucky.battletron.server.exceptionmapper;

import com.alltimeslucky.battletron.exception.BattletronException;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.Provider;

@Provider
public class BattletronExceptionMapper implements ExceptionMapper<BattletronException> {

    @Override
    public Response toResponse(BattletronException exception) {

        ExceptionDto dto = convertException(exception);
        return Response.status(400).entity(dto)
                .type(MediaType.APPLICATION_JSON).build();
    }


    private ExceptionDto convertException(BattletronException exception) {
        ExceptionDto dto = new ExceptionDto(exception.getCode().toString(), exception.getMessage(), exception.getField());
        for (BattletronException childException : exception.getExceptions()) {
            dto.add(convertException(childException));
        }

        return dto;
    }
}
