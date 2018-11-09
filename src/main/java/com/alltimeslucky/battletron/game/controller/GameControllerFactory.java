package com.alltimeslucky.battletron.game.controller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.model.Player;

/**
 * A GameController factory used to create instances of GameEngines.
 */
public class GameControllerFactory {

    /**
     * Instantiates a GameController with the specified values.
     *
     * @return An instance of GameController
     */
    public static GameController getLocalGameController(Player player1, Player player2,
                                                        PlayerController player1Controller, PlayerController player2Controller) {
        Game game = GameFactory.getGame(player1, player2);
        return new GameController(game, player1Controller, player2Controller);
    }

}