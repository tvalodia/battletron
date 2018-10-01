package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

import java.util.Random;

public class SmartPlayerAi implements PlayerAi {

    private Player player;

    public SmartPlayerAi(Player player) {
        this.player = player;
    }

    public Direction getDirection(int tick, GameState gameState) {

        int xVal = player.getPositionX();
        int yVal = player.getPositionY();

        int xLeftIncrement = 1;
        int xRightIncrement = 1;
        int yUpIncrement = 1;
        int yDownIncrement = 1;

        if (xVal == 0) {
            xLeftIncrement = 0;
        }

        if (xVal == 99) {
            xRightIncrement = 0;
        }

        if (yVal == 0) {
            yDownIncrement = 0;
        }

        if (yVal == 99) {
            yUpIncrement = 0;
        }

        int valUp = gameState.getPlayingField()[xVal][yVal + yUpIncrement];
        int valDown = gameState.getPlayingField()[xVal][yVal - yDownIncrement];
        int valLeft = gameState.getPlayingField()[xVal - xLeftIncrement][yVal];
        int valRight = gameState.getPlayingField()[xVal + xRightIncrement][yVal];

        if (xVal == 0) {
            valLeft = 5;
        }

        if (xVal == 99) {
            valRight = 5;
        }

        if (yVal == 0) {
            valDown = 5;
        }

        if (yVal == 99) {
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
