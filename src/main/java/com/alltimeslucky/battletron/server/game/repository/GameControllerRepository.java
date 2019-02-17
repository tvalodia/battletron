package com.alltimeslucky.battletron.server.game.repository;

import com.alltimeslucky.battletron.game.controller.GameController;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Stores all instances of games.
 */
public class GameControllerRepository {

    private Map<Long, GameController> data;

    public GameControllerRepository() {
        data = new ConcurrentHashMap<>();
    }

    /**
     * Inserts a new GameController into the repository.
     *
     * @param gameEngineId   The key to use for the GameController
     * @param gameController The GameController to insert.
     */
    public void add(long gameEngineId, GameController gameController) {
        data.put(gameEngineId, gameController);
    }

    public GameController get(Long gameEngineId) {
        return data.get(gameEngineId);
    }

    /**
     * Returns a sorted list in descending order of ID of all the games in this repository.
     *
     * @return A sorted list of games
     */
    public List<GameController> getAllGameControllers() {
        List<GameController> games = new ArrayList<>(data.values());
        games.sort((o1, o2) -> {
            if (o1.getGameId() < o2.getGameId()) {
                return 1;
            } else if (o1.getGameId() > o2.getGameId()) {
                return -1;
            } else {
                return 0;
            }
        });
        return games;
    }

    public void delete(long gameEngineId) {
        data.remove(gameEngineId);
    }

    public boolean contains(long gameId) {
        return data.containsKey(gameId);
    }
}
