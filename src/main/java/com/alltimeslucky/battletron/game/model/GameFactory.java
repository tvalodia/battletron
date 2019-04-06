package com.alltimeslucky.battletron.game.model;

import com.alltimeslucky.battletron.player.model.PlayerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class GameFactory {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private PlayerFactory playerFactory;
    private long gameCount;

    /**
     * Constructor.
     * @param playerFactory The PlayerFactory used to create new Players for the games.
     */
    @Autowired
    public GameFactory(PlayerFactory playerFactory) {

        this.playerFactory = playerFactory;
        this.gameCount = 0;
    }

    public Game get() {
        return new Game(++gameCount, WIDTH, HEIGHT, playerFactory.getPlayerOne(), playerFactory.getPlayerTwo());
    }
}
