package com.alltimeslucky.battletron.server.api.game;

import com.alltimeslucky.battletron.game.controller.GameController;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Comparator;
import java.util.List;
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

    public List<GameController> getAllGames() {
        List<GameController> games = new ArrayList<>(data.values());
        games.sort((o1, o2) -> {
            if (o1.getGameId() < o2.getGameId())
                return 1;
            else if (o1.getGameId() > o2.getGameId()) {
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
}
