package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

public class ExperimentPlayerAi implements PlayerController {

    private Player player;

    public ExperimentPlayerAi(Player player) {
        this.player = player;
        player.setReady(true);
    }

    /**
     * Making a separate AI class for experimentation.
     * @param gameState Used to determine the optimal direction in which the player should move.
     * @return Returns a DIRECTION to update player position.
     */

    public void execute(GameState gameState) {

        Direction outputVal = Direction.DOWN;

        if (player.getPositionX() >= 0) {

            outputVal = Direction.UP;

        }

        player.setDirection(outputVal);

    }

}