package com.alltimeslucky.battletron.game.model;

import com.alltimeslucky.battletron.player.model.PlayerFactory;

import java.util.GregorianCalendar;

import javax.inject.Inject;

public class GameFactory {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private PlayerFactory playerFactory;
    private long gameCount;

    @Inject
    public GameFactory(PlayerFactory playerFactory) {

        this.playerFactory = playerFactory;
        this.gameCount = 0;
    }

    public Game get() {
        return new Game(++gameCount, WIDTH, HEIGHT, playerFactory.getPlayerOne(), playerFactory.getPlayerTwo());
    }
}
