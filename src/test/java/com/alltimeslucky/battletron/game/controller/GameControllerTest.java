package com.alltimeslucky.battletron.game.controller;

import static org.junit.Assert.assertEquals;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.SimplePlayerAi;
import com.alltimeslucky.battletron.player.model.PlayerFactory;
import org.junit.Before;
import org.junit.Test;

public class GameControllerTest {

    private GameController gameController;

    private Game game;

    @Before
    public void setup() {
        PlayerFactory playerFactory = new PlayerFactory();
        GameFactory gameFactory = new GameFactory(playerFactory);
        GameControllerFactory gameControllerFactory = new GameControllerFactory();

        game = gameFactory.get();
        PlayerController playerOneController = new SimplePlayerAi(game.getPlayerOne());
        PlayerController playerTwoController = new SimplePlayerAi(game.getPlayerTwo());

        gameController = gameControllerFactory.get(game,
                playerOneController, playerTwoController);
    }

    @Test
    public void testGetGameId() {
        assertEquals(gameController.getGameId(), game.getId());
    }

}
