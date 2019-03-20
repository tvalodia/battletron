package com.alltimeslucky.battletron.config;

import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.controller.GameControllerRepository;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.server.game.api.GameApi;
import com.alltimeslucky.battletron.server.game.service.GameService;
import com.alltimeslucky.battletron.server.game.service.GameServiceImpl;
import com.alltimeslucky.battletron.server.session.repository.SessionRepository;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketController;
import com.google.inject.AbstractModule;

public class BattletronModule extends AbstractModule {
    @Override
    protected void configure() {
        bind(GameApi.class);
        bind(GameService.class).to(GameServiceImpl.class);
        bind(GameControllerRepository.class).asEagerSingleton();
        bind(SessionRepository.class).asEagerSingleton();
        bind(ClientWebSocketController.class).asEagerSingleton();
        bind(GameControllerFactory.class).asEagerSingleton();
        bind(GameFactory.class).asEagerSingleton();
    }
}
