package com.alltimeslucky.battletron.engine;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerAi;

import java.util.List;

/**
 * This class manages and executes the game loop; controls the flow of the game and notifies observers at every game tick.
 * With each tick, the engine will ask players for a direction input and update the game state accordingly.
 */
public class GameEngine {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private static final int TICK_INTERVAL_MILLIS = 50;
    private long lastTickTime;
    private List<GameStateListener> gameStateListeners;
    private Player player1;
    private Player player2;
    private final PlayerAi player1Ai;
    private final PlayerAi player2Ai;
    private GameState gameState;
    private int tick;

    /**
     * Constructor. Initialises the game state.
     *
     * @param gameStateListeners The observers to update after every game tick.
     * @param player1           Player 1
     * @param player2           Player 2
     * @param player1Ai         Player 1's controller
     * @param player2Ai         Player 2's controller
     */
    public GameEngine(List<GameStateListener> gameStateListeners, Player player1, Player player2, PlayerAi player1Ai, PlayerAi player2Ai) {
        this.gameStateListeners = gameStateListeners;
        this.player1 = player1;
        this.player2 = player2;
        this.player1Ai = player1Ai;
        this.player2Ai = player2Ai;
        this.lastTickTime = 0;
        gameState = new GameState(WIDTH, HEIGHT, player1, player2);
        tick = 0;
    }

    /**
     * Starts the game.
     */
    public void start() {
        gameState.setGameStatus(GameStatus.STARTED);
        run();
    }

    /**
     * The game loop. Gets moves from AIs, updates the playing field and checks for win conditions.
     */
    private void run() {
        while (gameState.getGameStatus() == GameStatus.STARTED) {
            long currentTime = System.currentTimeMillis();
            if (currentTime - lastTickTime >= TICK_INTERVAL_MILLIS) {
                //Gets each player's direction input
                Direction player1Direction = player1Ai.getDirection(tick, gameState);
                Direction player2Direction = player2Ai.getDirection(tick, gameState);

                //update the player given the AI's direction input
                player1.move(player1Direction);
                player2.move(player2Direction);

                //Check for collisions on the playing field
                if (gameState.isColliding(gameState.getPlayer1())) {
                    gameState.setGameStatus(GameStatus.WINNER);
                    gameState.setWinner(gameState.getPlayer2());
                } else if (gameState.isColliding(gameState.getPlayer2())) {
                    gameState.setGameStatus(GameStatus.WINNER);
                    gameState.setWinner(gameState.getPlayer1());
                } else if (gameState.isColliding(gameState.getPlayer1(), gameState.getPlayer2())) {
                    gameState.setGameStatus(GameStatus.DRAW);
                }

                //update the playing field
                gameState.updatePlayingField();
                tick++;

                //send an update to the observers
                gameStateListeners.forEach(gameStateListener -> gameStateListener.onGameStateUpdate(tick, gameState));
                lastTickTime = currentTime;//+ currentTime - lastTickTime - TICK_INTERVAL_MILLIS;
            }

        }
    }

}
