package com.tvalodia.battletron.engine;

import com.tvalodia.battletron.engine.gamestate.GameState;

import java.util.Random;

public class PlayerController {

    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    /*
    public Direction getDirection(int tick, GameState gameState) {
        return Direction.values()[new Random().nextInt(Direction.values().length)];
    }
    */

    public Direction getDirection(int tick, GameState gameState) {
        return Direction.values()[new Random().nextInt(2)];
    }

}