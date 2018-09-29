package com.tvalodia.battletron.engine;

import com.tvalodia.battletron.engine.gamestate.GameState;
import com.tvalodia.battletron.engine.gamestate.GameStateListener;
import com.tvalodia.battletron.engine.player.Player;
import com.tvalodia.battletron.engine.player.PlayerAI;

/**
 * This class manages and executes the game loop; controls the flow of the game and notifies observers at every game tick.
 * With each tick, the engine will ask players for a direction input and update the game state accordingly.
 */
public class GameEngine {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;
    private GameStateListener gameStateListener;
    private Player player1;
    private Player player2;
    private final PlayerAI player1AI;
    private final PlayerAI player2AI;
    private GameState gameState;
    private int tick;

    /**
     * Constructor. Initialises the game state.
     * @param gameStateListener The observer to update after every game tick.
     * @param player1 Player 1
     * @param player2 Player 2
     * @param player1AI Player 1's controller
     * @param player2AI Player 2's controller
     */
    public GameEngine(GameStateListener gameStateListener, Player player1, Player player2, PlayerAI player1AI, PlayerAI player2AI) {
        this.gameStateListener = gameStateListener;
        this.player1 = player1;
        this.player2 = player2;
        this.player1AI = player1AI;
        this.player2AI = player2AI;

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
     * The game loop.
     */
    public void run() {
        while (gameState.getGameStatus() == GameStatus.STARTED) {
            //Gets each player's direction input
            Direction player1Direction = player1AI.getDirection(tick, gameState);
            Direction player2Direction = player2AI.getDirection(tick, gameState);

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

            //send an update to the observer
            gameStateListener.onGameStateUpdate(tick, gameState);

            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

}
