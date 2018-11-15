package com.alltimeslucky.battletron.player.model;

import com.alltimeslucky.battletron.game.model.Game;

import java.util.GregorianCalendar;

public class PlayerFactory {

    public Player getPlayer1() {
        return new Player(1, 33, 50);
    }

    public Player getPlayer2() {
        return new Player(2, 66, 50);
    }
}
