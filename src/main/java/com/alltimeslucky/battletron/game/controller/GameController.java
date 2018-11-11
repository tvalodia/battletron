package com.alltimeslucky.battletron.game.controller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameStatus;
import com.alltimeslucky.battletron.player.controller.PlayerController;

/**
 * This class manages and executes the game loop; controls the flow of the game and notifies observers at every game tick.
 * With each tick, the engine will ask players for a direction input and update the game state accordingly.
 */
public class GameController extends Thread {

    private static final int TICK_INTERVAL_MILLIS = 50;
    private long lastTickTime;
    private final PlayerController player1Controller;
    private final PlayerController player2Controller;
    private Game game;
    private volatile boolean pauseThreadFlag;

    /**
     * Constructor. Initialises the game engine.
     *
     * @param game The Game model that holds the game state data
     * @param player1Controller  Player 1's AI controller
     * @param player2Controller  Player 2's AI controller
     */
    public GameController(Game game,
                          PlayerController player1Controller, PlayerController player2Controller) {
        this.player1Controller = player1Controller;
        this.player2Controller = player2Controller;
        this.lastTickTime = 0;
        this.game = game;
    }

    /**
     * The game loop. Gets moves from AIs, updates the playing field and checks for win conditions.
     */
    @Override
    public void run() {
        System.out.println("Game Engine started.");

        while (true) {
            if (interrupted()) {
                System.out.println("Game Engine killed.");
                return;
            }

            try {
                pauseIfRequired();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTickTime >= TICK_INTERVAL_MILLIS) {

                //Have the player controllers act given the current game
                player1Controller.execute(game);
                player2Controller.execute(game);

                //update the playing field
                game.update();

                if (game.getGameStatus() == GameStatus.COMPLETED_DRAW || game.getGameStatus() == GameStatus.COMPLETED_WINNER) {
                    break;
                }

                lastTickTime = currentTime;//+ currentTime - lastTickTime - TICK_INTERVAL_MILLIS;
            }
        }

        System.out.println("Game Engine stopped.");
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
}
