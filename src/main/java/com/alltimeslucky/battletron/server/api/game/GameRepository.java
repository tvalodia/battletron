package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.engine.GameEngine;

import java.util.Collection;
import java.util.HashMap;

/**
 * Store all instances of started games.
 */
public class GameRepository {

    private static GameRepository instance;
    private HashMap<Long, GameEngine> data;


    private GameRepository() {
        data = new HashMap<>();
    }

    /**
     * Returns the singleton instance of the Game Repository.
     * @return
     */
    public static GameRepository getInstance() {
        if (instance == null) {
            instance = new GameRepository();
        }
        return instance;
    }

    public void addGameEngine(long gameEngineId, GameEngine gameEngine) {
        data.put(gameEngineId, gameEngine);
    }

    public GameEngine getGameEngine(long gameEngineId) {
        return data.get(gameEngineId);
    }

    public Collection<GameEngine> getAllGameEngines() {
        return data.values();
    }

    public void delete(long gameEngineId) {
        data.remove(gameEngineId);
    }
}
