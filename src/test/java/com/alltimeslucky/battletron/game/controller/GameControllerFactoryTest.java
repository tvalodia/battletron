package com.alltimeslucky.battletron.game.controller;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNotSame;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.SimplePlayerAi;
import com.alltimeslucky.battletron.player.model.PlayerFactory;
import org.junit.Before;
import org.junit.Test;

public class GameControllerFactoryTest {

    private PlayerFactory playerFactory = new PlayerFactory();
    private GameFactory gameFactory = new GameFactory(playerFactory);
    private GameControllerFactory gameControllerFactory = new GameControllerFactory();

    private Game game;
    private PlayerController playerOneController;
    private PlayerController playerTwoController;

    private GameController gameController;

    @Before
    public void setup() {
        game = gameFactory.get();
        playerOneController = new SimplePlayerAi(game.getPlayerOne());
        playerTwoController = new SimplePlayerAi(game.getPlayerTwo());
    }

    @Test
    public void testGameControllerFromFactoryIsNotNull() {
        gameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);

        assertNotNull(gameController);
    }

    @Test
    public void testGameControllerFromFactoryIsNotSingletonPooledOrCached() {
        gameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);

        GameController anotherGameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);

        assertNotSame(anotherGameController, gameController);
    }

}
