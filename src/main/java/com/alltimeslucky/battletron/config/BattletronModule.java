package com.alltimeslucky.battletron.config;

import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.server.game.api.GameApi;
import com.alltimeslucky.battletron.server.game.repository.GameControllerRepository;
import com.alltimeslucky.battletron.server.game.service.GameService;
import com.alltimeslucky.battletron.server.game.service.GameServiceImpl;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameUpdateRouter;
import com.google.inject.AbstractModule;

public class BattletronModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameApi.class);
        bind(GameService.class).to(GameServiceImpl.class);
        bind(GameControllerRepository.class).asEagerSingleton();
        bind(ClientWebSocketRepository.class).asEagerSingleton();
        bind(WebSocketGameUpdateRouter.class).asEagerSingleton();
        bind(GameControllerFactory.class).asEagerSingleton();
        bind(GameFactory.class).asEagerSingleton();
    }
}
