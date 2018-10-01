package com.alltimeslucky.battletron.engine.gamestate;

/**
 * This interface describes how listening clients will receive game state updates.
 */
public interface GameStateListener {

    /**
     * This method is called with each tick of the game engine.
     * @param tick The current tick
     * @param gameState The current game state
     */
    void onGameStateUpdate(int tick, GameState gameState);
}
