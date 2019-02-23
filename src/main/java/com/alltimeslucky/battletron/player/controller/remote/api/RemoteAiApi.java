package com.alltimeslucky.battletron.player.controller.remote.api;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.PUT;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

public interface RemoteAiApi {

    @PUT
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.TEXT_PLAIN)
    String getDirection(GameDto game) throws BattletronException;
}
