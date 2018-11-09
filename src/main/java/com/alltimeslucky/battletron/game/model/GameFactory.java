package com.alltimeslucky.battletron.game.model;

import com.alltimeslucky.battletron.player.model.Player;

import java.util.GregorianCalendar;

public class GameFactory {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    public static Game getGame(Player player1, Player player2) {
        return new Game(GregorianCalendar.getInstance().getTimeInMillis(), WIDTH, HEIGHT, player1, player2);
    }
}
