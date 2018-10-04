package com.alltimeslucky.battletron.engine;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerController;

import java.util.GregorianCalendar;
import java.util.List;

/**
 * This class manages and executes the game loop; controls the flow of the game and notifies observers at every game tick.
 * With each tick, the engine will ask players for a direction input and update the game state accordingly.
 */
public class GameEngine extends Thread {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static final int TICK_INTERVAL_MILLIS = 100;
    private long id;
    private long lastTickTime;
    private List<GameStateListener> gameStateListeners;
    private final PlayerController player1Controller;
    private final PlayerController player2Controller;
    private GameState gameState;
    private volatile boolean kill;
    private volatile boolean pauseThreadFlag;

    /**
     * Constructor. Initialises the game state.
     *
     * @param gameStateListeners The observers to update after every game tick.
     * @param player1            The instance of Player that represents Player 1 of the GameState
     * @param player2            The instance of Player that represents Player 2 of the GameState
     * @param player1Controller  Player 1's AI controller
     * @param player2Controller  Player 2's AI controller
     */
    public GameEngine(List<GameStateListener> gameStateListeners, Player player1, Player player2,
                      PlayerController player1Controller, PlayerController player2Controller) {
        this.gameStateListeners = gameStateListeners;
        this.player1Controller = player1Controller;
        this.player2Controller = player2Controller;
        this.id = GregorianCalendar.getInstance().getTimeInMillis();
        this.lastTickTime = 0;
        gameState = new GameState(WIDTH, HEIGHT, player1, player2);
    }

    /**
     * The game loop. Gets moves from AIs, updates the playing field and checks for win conditions.
     */
    @Override
    public void run() {
        System.out.println("Game Engine started.");
        gameState.setGameStatus(GameStatus.STARTED);
        kill = false;
        while (gameState.getGameStatus() == GameStatus.STARTED) {
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
                //Gets each player's direction input
                Direction player1Direction = player1Controller.getDirection(gameState);
                Direction player2Direction = player2Controller.getDirection(gameState);

                //update the playing field
                gameState.update(player1Direction, player2Direction);

                //send an update to the observers
                gameStateListeners.forEach(gameStateListener -> gameStateListener.onGameStateUpdate(gameState));
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

    /**
     * Returns the id of the game.
     * @return
     */
    public long getId() {
        return id;
    }
}
