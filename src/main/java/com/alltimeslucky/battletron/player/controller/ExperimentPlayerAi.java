package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.player.model.Player;

public class ExperimentPlayerAi implements PlayerController {

    private Player player;

    public ExperimentPlayerAi(Player player) {
        this.player = player;
        player.setReady(true);
    }

    /**
     * Making a separate AI class for experimentation.
     * @param game Used to determine the optimal direction in which the player should move.
     */

    public void execute(Game game) {

        Direction outputVal = Direction.DOWN;

        if (player.getPositionX() >= 0) {

            outputVal = Direction.UP;

        }

        player.setDirection(outputVal);

    }

}