package com.alltimeslucky.battletron.engine.player;

import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.gamestate.GameState;

import java.util.Random;

public class DownLeftPlayerAi implements PlayerAi {

    private Player player;

    public DownLeftPlayerAi(Player player) {
        this.player = player;
    }

    public Direction getDirection(GameState gameState) {
        return Direction.values()[new Random().nextInt(2)];
    }

}
