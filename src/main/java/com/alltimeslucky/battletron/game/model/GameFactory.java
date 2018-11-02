package com.alltimeslucky.battletron.game.model;

import com.alltimeslucky.battletron.player.model.Player;

import java.util.GregorianCalendar;

public class GameFactory {

    public static Game getGameState(int width, int height, Player player1, Player player2) {
        return new Game(GregorianCalendar.getInstance().getTimeInMillis(), width, height, player1, player2);
    }
}
