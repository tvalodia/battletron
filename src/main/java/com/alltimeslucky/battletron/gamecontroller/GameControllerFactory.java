package com.alltimeslucky.battletron.gamecontroller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.controller.PlayerController;

import org.springframework.stereotype.Component;

/**
 * A GameController factory used to create instances of GameControllers.
 */
@Component
public class GameControllerFactory {

    /**
     * Instantiates a GameController with the specified values.
     *
     * @return An instance of GameController
     */
    public GameController get(Game game, PlayerController playerOneController, PlayerController playerTwoController) {
        return new GameController(game, playerOneController, playerTwoController);
    }

}