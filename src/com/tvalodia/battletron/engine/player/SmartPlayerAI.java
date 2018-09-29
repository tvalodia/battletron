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

        int xIncrement = 1;
        int yIncrement = 1;

        if (xVal == 0 || xVal == 99) {
            xIncrement = 0;
        }

        if (yVal == 0 || yVal == 99) {
            yIncrement = 0;
        }

        int valUp = gameState.getPlayingField()[xVal][yVal + yIncrement];
        int valDown = gameState.getPlayingField()[xVal][yVal - yIncrement];
        int valLeft = gameState.getPlayingField()[xVal - xIncrement][yVal];
        int valRight = gameState.getPlayingField()[xVal + xIncrement][yVal];

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
