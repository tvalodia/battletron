package com.alltimeslucky.battletron.gamecontroller;

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
public class GameControllerRepository {

    private static final Logger LOG = LogManager.getLogger();
    private static final int MAX_SIZE = 100;

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
        clearOld();
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

    private void clearOld() {
        if (data.size() > MAX_SIZE) {
            LOG.info(String.format("Clearing old game controllers from the repository. Size: %d", data.size()));
            List<GameController> gameControllers = getAllGameControllers();
            for (int i = gameControllers.size() - 1; i >= 0; i--) {
                GameController controller = gameControllers.get(i);
                if (!controller.getGame().getGameStatus().equals(GameStatus.WAITING_FOR_READY)
                        || !controller.getGame().getGameStatus().equals(GameStatus.STARTED)) {
                    LOG.info(String.format("Removing game %d from the repository", controller.getGameId()));
                    data.remove(controller.getGameId());
                }

                if (data.size() <= MAX_SIZE) {
                    break;
                }
            }

            LOG.info(String.format("New game controller repository size: %d", data.size()));
        }
    }

}
