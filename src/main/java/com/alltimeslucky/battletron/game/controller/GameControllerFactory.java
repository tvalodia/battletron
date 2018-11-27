package com.alltimeslucky.battletron.game.controller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.controller.PlayerController;

/**
 * A GameController factory used to create instances of GameControllers.
 */
public class GameControllerFactory {

    /**
     * Instantiates a GameController with the specified values.
     *
     * @return An instance of GameController
     */
    public GameController get(Game game,
                              PlayerController playerOneController, PlayerController playerTwoController) {
        return new GameController(game, playerOneController, playerTwoController);
    }

}