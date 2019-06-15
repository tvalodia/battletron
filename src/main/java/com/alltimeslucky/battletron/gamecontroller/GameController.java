package com.alltimeslucky.battletron.gamecontroller;

import com.alltimeslucky.battletron.exception.BattletronException;
import com.alltimeslucky.battletron.exception.ExceptionCode;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameStatus;
import com.alltimeslucky.battletron.player.controller.PlayerController;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class manages and executes the game loop; controls the flow of the game and notifies observers at every game tick.
 * With each tick, the engine will ask players for a direction input and update the game state accordingly.
 */
public class GameController extends Thread {

    private static final Logger LOG = LogManager.getLogger();

    private static final int DEFAULT_TICK_INTERVAL_MILLIS = 50;
    private static final long TIMEOUT = 1000 * 60 * 5;
    private long lastTickTime;
    private PlayerController playerOneController;
    private PlayerController playerTwoController;
    private Game game;
    private volatile boolean pauseThreadFlag;
    private int tickIntervalMillis;

    /**
     * Constructor. Initialises the game engine.
     *
     * @param game The Game model that holds the game state data
     * @param playerOneController  Player 1's AI controller
     * @param playerTwoController  Player 2's AI controller
     */
    GameController(Game game, PlayerController playerOneController, PlayerController playerTwoController) {
        this.playerOneController = playerOneController;
        this.playerTwoController = playerTwoController;
        this.lastTickTime = 0;
        this.game = game;
        this.tickIntervalMillis = DEFAULT_TICK_INTERVAL_MILLIS;
    }

    /**
     * The game loop. Gets moves from AIs, updates the playing field and checks for win conditions.
     */
    @Override
    public void run() {
        LOG.info(String.format("Game engine %d started", getGameId()));

        while (true) {
            if (interrupted()) {
                game.stop();
                LOG.info(String.format("Game engine %d killed", getGameId()));
                return;
            }

            try {
                pauseIfRequired();
            } catch (InterruptedException e) {
                LOG.error(ExceptionCode.UNABLE_TO_PAUSE);
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTickTime >= tickIntervalMillis) {

                if (game.getGameStatus().equals(GameStatus.WAITING_FOR_READY) && currentTime - game.getCreatedDate() > TIMEOUT) {
                    game.timeout();
                    LOG.warn(String.format("Game engine %d stopped - waited too long for STARTED state", getGameId()));
                    return;
                }

                try {
                    //Execute the player controllers
                    if (playerOneController != null) {
                        playerOneController.execute(game);
                    }

                    if (playerTwoController != null) {
                        playerTwoController.execute(game);
                    }
                } catch (BattletronException e) {
                    LOG.error(e);
                    this.kill();
                }

                //update the playing field
                game.update();

                if (game.getGameStatus() == GameStatus.COMPLETED_DRAW || game.getGameStatus() == GameStatus.COMPLETED_WINNER) {
                    break;
                }

                lastTickTime = currentTime;
            }
        }

        LOG.info(String.format("Game engine %d stopped", getGameId()));
    }

    /**
     * Request that the game be paused.
     */
    public void pauseThread() {
        pauseThreadFlag = true;
    }

    /**
     * Request that the game be resumed.
     */
    public void resumeThread() {
        if (this.getState().equals(Thread.State.WAITING)) {
            synchronized (this) {
                notify();
                pauseThreadFlag = false;
            }
        }
    }

    /**
     * Kills the game.
     */
    public void kill() {
        if (this.getState().equals(Thread.State.WAITING)) {
            synchronized (this) {
                notify();
                pauseThreadFlag = false;
            }
        }
        synchronized (this) {
            interrupt();
        }
    }

    /**
     * Pauses the thread.
     * @throws InterruptedException Thrown when an error occurs.
     */
    private void pauseIfRequired() throws InterruptedException {
        if (pauseThreadFlag) {
            if (this.getState().equals(Thread.State.RUNNABLE)) {
                synchronized (this) {
                    wait();
                    pauseThreadFlag = false;
                }
            }
        }
    }

    public long getGameId() {
        return game.getId();
    }

    public Game getGame() {
        return game;
    }

    public PlayerController getPlayerOneController() {
        return playerOneController;
    }

    public PlayerController getPlayerTwoController() {
        return playerTwoController;
    }

    /**
     * Assigns the given PlayerController to either player one or player two.
     * @param playerController The new PlayerController to be used
     */
    public void joinGame(PlayerController playerController) {
        if (playerOneController == null) {
            this.playerOneController = playerController;
        } else if (playerTwoController == null) {
            this.playerTwoController = playerController;
        }
    }

    /**
     * Removes the given PlayerController from the game.
     * @param playerController The PlayerController to remove
     */
    public void leaveGame(PlayerController playerController) {
        if (playerOneController == playerController) {
            playerOneController = null;
        } else if (playerTwoController == playerController) {
            playerTwoController = null;
        }
    }

    public boolean isJoinable() {
        return (playerOneController == null || playerTwoController == null) && game.getGameStatus() == GameStatus.WAITING_FOR_READY;
    }

    public void setTickIntervalMillis(int tickIntervalMillis) {
        this.tickIntervalMillis = tickIntervalMillis;
    }
}
