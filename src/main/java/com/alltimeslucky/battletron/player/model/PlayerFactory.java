package com.alltimeslucky.battletron.player.model;

public class PlayerFactory {

    public Player getPlayerOne() {
        return new Player(1, 33, 50);
    }

    public Player getPlayerTwo() {
        return new Player(2, 66, 50);
    }
}
