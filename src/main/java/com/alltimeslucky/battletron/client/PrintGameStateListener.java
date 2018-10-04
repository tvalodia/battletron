package com.alltimeslucky.battletron.client;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.google.gson.Gson;

/**
 * This class just prints a json formatted version of the GameState at each tick.
 */
public class PrintGameStateListener implements GameStateListener {

    private Gson gson;

    public PrintGameStateListener() {
        gson = new Gson();
    }

    @Override
    public void onGameStateUpdate(GameState gameState) {
        System.out.println(gson.toJson(gameState));
    }
}
