package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

import java.util.Random;

public class SimplePlayerAi implements PlayerController {

    private Player player;

    public SimplePlayerAi(Player player) {
        this.player = player;
    }

    /**
     * Graham testing javadoc comments.
     * @param gameState Big variable containing all the info about where the game is at homie.
     * @return Returns a DIRECTION to update player position.
     */

    public Direction getDirection(GameState gameState) {

        int xval = player.getPositionX();
        int yval = player.getPositionY();

        int leftIncrement = 1;
        int rightIncrement = 1;
        int upIncrement = 1;
        int downIncrement = 1;

        if (xval == 0) {
            leftIncrement = 0;
        }

        if (xval == 99) {
            rightIncrement = 0;
        }

        if (yval == 0) {
            downIncrement = 0;
        }

        if (yval == 99) {
            upIncrement = 0;
        }

        int valUp = gameState.getPlayingField()[xval][yval + upIncrement];
        int valDown = gameState.getPlayingField()[xval][yval - downIncrement];
        int valLeft = gameState.getPlayingField()[xval - leftIncrement][yval];
        int valRight = gameState.getPlayingField()[xval + rightIncrement][yval];

        if (xval == 0) {
            valLeft = 5;
        }

        if (xval == 99) {
            valRight = 5;
        }

        if (yval == 0) {
            valDown = 5;
        }

        if (yval == 99) {
            valUp = 5;
        }

        boolean validOutput = false;
        Direction outputVal = Direction.values()[new Random().nextInt(Direction.values().length)];

        while (!validOutput) {

            outputVal = Direction.values()[new Random().nextInt(Direction.values().length)];

            if (outputVal == Direction.UP && valUp == 0) {
                validOutput = true;
            }

            if (outputVal == Direction.DOWN && valDown == 0) {
                validOutput = true;
            }

            if (outputVal == Direction.LEFT && valLeft == 0) {
                validOutput = true;
            }

            if (outputVal == Direction.RIGHT && valRight == 0) {
                validOutput = true;
            }

            if (valUp != 0 && valDown != 0 && valLeft != 0 && valRight != 0) {
                validOutput = true;
            }

        }

        return outputVal;

    }

}
