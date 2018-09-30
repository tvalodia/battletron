package com.tvalodia.battletron;

import com.tvalodia.battletron.engine.GameEngine;
import com.tvalodia.battletron.engine.player.Player;
import com.tvalodia.battletron.engine.player.PlayerAI;
import com.tvalodia.battletron.engine.player.SmartPlayerAI;
import com.tvalodia.battletron.ui.BattletronWindow;

public class Battletron {

    private GameEngine gameEngine;
    private BattletronWindow window;

    public static void main(String[] args) {
        new Battletron().play();
    }

    public Battletron() {
        window = new BattletronWindow();
        window.initialise();
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

        gameEngine = createEngine();
    }

    public void play() {
        gameEngine = createEngine();
        gameEngine.start();
        System.out.println("Complete");
    }

    public GameEngine createEngine() {
        Player player1 = new Player(1, 0, 0);
        Player player2 = new Player(2, 99, 99);
        PlayerAI player1Controller = new SmartPlayerAI(player1);
        PlayerAI player2Controller = new SmartPlayerAI(player2);
        GameEngine gameEngine = new GameEngine(window.getGameStateListener(), player1, player2, player1Controller, player2Controller);
        return gameEngine;
    }
}
