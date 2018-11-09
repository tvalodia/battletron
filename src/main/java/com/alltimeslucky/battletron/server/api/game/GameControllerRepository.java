package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.game.controller.GameController;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores all instances of games.
 */
public class GameControllerRepository {

    private static GameControllerRepository instance;
    private ConcurrentHashMap<Long, GameController> data;


    private GameControllerRepository() {
        data = new ConcurrentHashMap<>();
    }

    /**
     * Returns the singleton instance of the Game Repository.
     * @return The singleton instance of the GameControllerRepository.
     */
    public static GameControllerRepository getInstance() {
        if (instance == null) {
            instance = new GameControllerRepository();
        }
        return instance;
    }

    public void add(long gameEngineId, GameController gameController) {
        data.put(gameEngineId, gameController);
    }

    public GameController get(long gameEngineId) {
        return data.get(gameEngineId);
    }

    public Collection<GameController> getAllGameEngines() {
        return data.values();
    }

    public void delete(long gameEngineId) {
        data.remove(gameEngineId);
    }
}
