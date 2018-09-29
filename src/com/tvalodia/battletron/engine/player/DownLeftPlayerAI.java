package com.tvalodia.battletron.engine.player;

import com.tvalodia.battletron.engine.Direction;
import com.tvalodia.battletron.engine.gamestate.GameState;

import java.util.Random;

public class DownLeftPlayerAI implements PlayerAI {

    private Player player;

    public DownLeftPlayerAI(Player player) {
        this.player = player;
    }

    public Direction getDirection(int tick, GameState gameState) {
        return Direction.values()[new Random().nextInt(2)];
    }

}
