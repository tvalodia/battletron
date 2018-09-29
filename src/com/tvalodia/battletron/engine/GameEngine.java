package com.tvalodia.battletron.engine;

import com.tvalodia.battletron.client.GameClient;

public class GameEngine {

    boolean running;

    private GameClient gameClient;
    private final PlayerController player1Controller;
    private final PlayerController player2Controller;
    private GameState gameState;
    private int tick;

    public GameEngine(GameClient gameClient,
                      Player player1, Player player2,
                      PlayerController player1Controller, PlayerController player2Controller) {
        this.gameClient = gameClient;
        this.player1Controller = player1Controller;
        this.player2Controller = player2Controller;

        gameState = new GameState(100, 100, player1, player2);
        tick = 0;
    }

    public void start() {
        gameState.setLevelStatus(LevelStatus.STARTED);
        run();
    }

    public void run() {
        while (gameState.getLevelStatus() == LevelStatus.STARTED) {
            System.out.println(tick);
            Direction player1Direction = player1Controller.getDirection(tick, gameState);
            Direction player2Direction = player2Controller.getDirection(tick, gameState);
            gameState.updatePlayer(gameState.getPlayer1(), player1Direction);
            gameState.updatePlayer(gameState.getPlayer2(), player2Direction);

            if (gameState.isCollision(gameState.getPlayer1())) {
                gameState.setLevelStatus(LevelStatus.WINNER);
                gameState.setWinner(gameState.getPlayer2());
            } else if (gameState.isCollision(gameState.getPlayer2())) {
                gameState.setLevelStatus(LevelStatus.WINNER);
                gameState.setWinner(gameState.getPlayer1());
            } else if (gameState.isCollision(gameState.getPlayer1(), gameState.getPlayer2())) {
                gameState.setLevelStatus(LevelStatus.DRAW);
            }

            gameState.updateLevel();
            tick++;
            gameClient.update(tick, gameState);
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

}
