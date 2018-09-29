package com.tvalodia.tronbattle.engine;

import java.util.Random;

public class PlayerController {

    private Player player;

    public PlayerController(Player player) {
        this.player = player;
    }

    public Direction getDirection(int tick, GameState gameState) {
        return Direction.values()[new Random().nextInt(Direction.values().length)];
    }
}
