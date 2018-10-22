package com.alltimeslucky.battletron.engine;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.player.PlayerController;
import com.alltimeslucky.battletron.server.api.game.GameRepository;

/**
 * This class manages and executes the game loop; controls the flow of the game and notifies observers at every game tick.
 * With each tick, the engine will ask players for a direction input and update the game state accordingly.
 */
public class GameEngine extends Thread {

    private static final int TICK_INTERVAL_MILLIS = 200;
    private long lastTickTime;
    private final PlayerController player1Controller;
    private final PlayerController player2Controller;
    private GameState gameState;
    private volatile boolean pauseThreadFlag;

    /**
     * Constructor. Initialises the game engine.
     *
     * @param gameState The GameState model that holds the game state data
     * @param player1Controller  Player 1's AI controller
     * @param player2Controller  Player 2's AI controller
     */
    public GameEngine(GameState gameState,
                      PlayerController player1Controller, PlayerController player2Controller) {
        this.player1Controller = player1Controller;
        this.player2Controller = player2Controller;
        this.lastTickTime = 0;
        this.gameState = gameState;
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

                //Have the player controllers act given the current gamestate
                player1Controller.execute(gameState);
                player2Controller.execute(gameState);

                //update the playing field
                gameState.update();

                if (gameState.getGameStatus() == GameStatus.COMPLETED_DRAW || gameState.getGameStatus() == GameStatus.COMPLETED_WINNER) {
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

    @Override
    public long getId() {
        return gameState.getId();
    }

    public GameState getGameState() {
        return gameState;
    }
}
