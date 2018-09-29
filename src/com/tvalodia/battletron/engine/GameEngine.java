package com.tvalodia.battletron.engine;

import com.tvalodia.battletron.engine.gamestate.GameState;
import com.tvalodia.battletron.engine.gamestate.GameStateListener;

public class GameEngine {

    private GameStateListener gameStateListener;
    private final PlayerController player1Controller;
    private final PlayerController player2Controller;
    private GameState gameState;
    private int tick;

    public GameEngine(GameStateListener gameStateListener, Player player1, Player player2,
                      PlayerController player1Controller, PlayerController player2Controller) {
        this.gameStateListener = gameStateListener;
        this.player1Controller = player1Controller;
        this.player2Controller = player2Controller;

        gameState = new GameState(100, 100, player1, player2);
        tick = 0;
    }

    public void start() {
        gameState.setGameStatus(GameStatus.STARTED);
        run();
    }

    public void run() {
        while (gameState.getGameStatus() == GameStatus.STARTED) {
            System.out.println(tick);
            Direction player1Direction = player1Controller.getDirection(tick, gameState);
            Direction player2Direction = player2Controller.getDirection(tick, gameState);
            gameState.updatePlayer(gameState.getPlayer1(), player1Direction);
            gameState.updatePlayer(gameState.getPlayer2(), player2Direction);

            if (gameState.isColliding(gameState.getPlayer1())) {
                gameState.setGameStatus(GameStatus.WINNER);
                gameState.setWinner(gameState.getPlayer2());
            } else if (gameState.isColliding(gameState.getPlayer2())) {
                gameState.setGameStatus(GameStatus.WINNER);
                gameState.setWinner(gameState.getPlayer1());
            } else if (gameState.isColliding(gameState.getPlayer1(), gameState.getPlayer2())) {
                gameState.setGameStatus(GameStatus.DRAW);
            }

            gameState.updatePlayingField();
            tick++;
            gameStateListener.onGameStateUpdate(tick, gameState);
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
