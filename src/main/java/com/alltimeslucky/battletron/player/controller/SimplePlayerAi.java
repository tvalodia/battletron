package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.player.model.Player;

import java.util.Random;

public class SimplePlayerAi implements PlayerController {

    private Player player;

    public SimplePlayerAi(Player player) {
        this.player = player;
        player.setReady(true);
    }

    /**
     * Graham testing javadoc comments.
     * @param game Big variable containing all the info about where the game is at homie.
     */

    public void execute(Game game) {

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

        int valUp = game.getPlayingField()[xval][yval + upIncrement];
        int valDown = game.getPlayingField()[xval][yval - downIncrement];
        int valLeft = game.getPlayingField()[xval - leftIncrement][yval];
        int valRight = game.getPlayingField()[xval + rightIncrement][yval];

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

        player.setDirection(outputVal);

    }

}
