package com.alltimeslucky.battletron.game.model;

import com.alltimeslucky.battletron.exception.BattletronException;

/**
 * This interface describes how listening clients will receive game state updates.
 */
public interface GameListener {

    /**
     * This method is called with each tick of the game engine.
     * @param game The current game state
     */
    void onGameStateUpdate(Game game) throws BattletronException;
}
