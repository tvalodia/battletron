package com.tvalodia.battletron.engine.player;

import com.tvalodia.battletron.engine.Direction;
import com.tvalodia.battletron.engine.gamestate.GameState;

import java.util.Random;

public class SmartPlayerAI implements PlayerAI {

    private Player player;

    public SmartPlayerAI(Player player) {
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
            yUpIncrement = 0;
        }

        if (yVal == 99) {
            yDownIncrement = 0;
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
            valUp = 5;
        }

        if (yVal == 99) {
            valDown = 5;
        }

        Direction outputVal = Direction.UP;

        if (valUp == 0) {
            outputVal = Direction.UP;
        }

        if (valDown == 0) {
            outputVal = Direction.DOWN;
        }

        if (valLeft == 0) {
            outputVal = Direction.LEFT;
        }

        if (valRight == 0) {
            outputVal = Direction.RIGHT;
        }

        return outputVal;

    }

}
