package com.alltimeslucky.battletron.game.repository;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameStatus;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Repository;

/**
 * Stores all instances of games.
 */
@Repository
public class GameRepository {

    private static final Logger LOG = LogManager.getLogger();
    private static final int MAX_SIZE = 100;

    private Map<Long, Game> data;

    public GameRepository() {
        data = new ConcurrentHashMap<>();
    }

    /**
     * Inserts a new Game into the repository.
     *
     * @param gameEngineId   The key to use for the Game
     * @param gameController The Game to insert.
     */
    public void add(long gameEngineId, Game gameController) {
        clearOld();
        data.put(gameEngineId, gameController);
    }

    public Game get(Long gameEngineId) {
        return data.get(gameEngineId);
    }

    /**
     * Returns a sorted list in descending order of ID of all the games in this repository.
     *
     * @return A sorted list of games
     */
    public List<Game> getAllGames() {
        List<Game> games = new ArrayList<>(data.values());
        games.sort((o1, o2) -> {
            if (o1.getId() < o2.getId()) {
                return 1;
            } else if (o1.getId() > o2.getId()) {
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

    private void clearOld() {
        if (data.size() > MAX_SIZE) {
            LOG.info(String.format("Clearing old game controllers from the repository. Size: %d", data.size()));
            List<Game> games = getAllGames();
            for (int i = games.size() - 1; i >= 0; i--) {
                Game game = games.get(i);
                if (!game.getGameStatus().equals(GameStatus.WAITING_FOR_READY)
                        || !game.getGameStatus().equals(GameStatus.STARTED)) {
                    LOG.info(String.format("Removing game %d from the repository", game.getId()));
                    data.remove(game.getId());
                }

                if (data.size() <= MAX_SIZE) {
                    break;
                }
            }

            LOG.info(String.format("New game repository size: %d", data.size()));
        }
    }

}
