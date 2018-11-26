package com.alltimeslucky.battletron.player.model;

import com.alltimeslucky.battletron.game.model.Game;

import java.util.GregorianCalendar;

public class PlayerFactory {

    public Player getPlayerOne() {
        return new Player(1, 33, 50);
    }

    public Player getPlayerTwo() {
        return new Player(2, 66, 50);
    }
}
