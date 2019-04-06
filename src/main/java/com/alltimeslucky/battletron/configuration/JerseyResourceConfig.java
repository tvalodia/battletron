package com.alltimeslucky.battletron.configuration;

import com.alltimeslucky.battletron.server.exceptionmapper.BattletronExceptionMapper;
import com.alltimeslucky.battletron.server.game.api.GameApi;

import org.glassfish.jersey.server.ResourceConfig;

public class JerseyResourceConfig extends ResourceConfig {

    public JerseyResourceConfig() {
        register(GameApi.class);
        register(BattletronExceptionMapper.class);
    }

}
