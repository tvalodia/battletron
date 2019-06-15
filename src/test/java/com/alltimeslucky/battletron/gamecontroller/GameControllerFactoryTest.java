package com.alltimeslucky.battletron.gamecontroller;

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

    private GameControllerFactory gameControllerFactory;

    private Game game;
    private PlayerController playerOneController;
    private PlayerController playerTwoController;

    /**
     * We want a fresh instance of gameControllerFactory to test each time. We need some other attributes in order to
     * run the get() method in the tests.
     */
    @Before
    public void setup() {
        PlayerFactory playerFactory = new PlayerFactory();
        GameFactory gameFactory = new GameFactory(playerFactory);
        gameControllerFactory = new GameControllerFactory();

        game = gameFactory.get();
        playerOneController = new SimplePlayerAi(game.getPlayerOne());
        playerTwoController = new SimplePlayerAi(game.getPlayerTwo());
    }

    @Test
    public void testGameControllerFromFactoryIsNotNull() {
        GameController gameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);

        assertNotNull(gameController);
    }

    @Test
    public void testGameControllerFromFactoryIsNotSingletonPooledOrCached() {
        GameController gameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);

        GameController anotherGameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);

        assertNotSame(anotherGameController, gameController);
    }

}
