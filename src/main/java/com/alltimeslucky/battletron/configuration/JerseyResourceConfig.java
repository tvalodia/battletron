package com.alltimeslucky.battletron.configuration;

import com.alltimeslucky.battletron.server.exceptionmapper.BattletronExceptionMapper;
import com.alltimeslucky.battletron.server.game.api.GameApi;
import com.alltimeslucky.battletron.server.trainer.api.TrainingGameApi;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
        register(GameApi.class);
        register(TrainingGameApi.class);
        register(BattletronExceptionMapper.class);
    }

}
