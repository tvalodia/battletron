package com.alltimeslucky.battletron.engine.gamestate;

import com.alltimeslucky.battletron.engine.player.Player;

import java.util.GregorianCalendar;
import java.util.List;

public class GameStateFactory {

    public static GameState getGameState(int width, int height, Player player1, Player player2,
                                         List<GameStateListener> gameStateListeners) {
        return new GameState(GregorianCalendar.getInstance().getTimeInMillis(), width, height, player1, player2, gameStateListeners);
    }
}
