package com.alltimeslucky.battletron.game.controller;

import static org.junit.Assert.assertNotNull;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.SimplePlayerAi;
import com.alltimeslucky.battletron.player.model.PlayerFactory;
import org.junit.Before;
import org.junit.Test;

public class GameControllerTest {

    private PlayerFactory playerFactory = new PlayerFactory();
    private GameFactory gameFactory = new GameFactory(playerFactory);
    private GameControllerFactory gameControllerFactory = new GameControllerFactory();
    private GameController gameController;

    @Before
    public void setup() {
        Game game = gameFactory.get();
        PlayerController playerOneController = new SimplePlayerAi(game.getPlayerOne());
        PlayerController playerTwoController = new SimplePlayerAi(game.getPlayerTwo());
        gameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);
    }

    @Test
    public void testGameControllerFromFactoryIsNotNull() {
        assertNotNull(gameController);
    }

}
/*Placeholder for now.*/
