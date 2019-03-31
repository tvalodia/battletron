package com.alltimeslucky.battletron.trainer.api;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.core.MediaType;

public interface TrainerApi {

    @POST
    @Consumes(MediaType.APPLICATION_JSON)
    void update(GameDto game) throws BattletronException;
}
