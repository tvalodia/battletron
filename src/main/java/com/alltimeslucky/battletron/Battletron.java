package com.alltimeslucky.battletron;

import com.alltimeslucky.battletron.client.PrintListener;
import com.alltimeslucky.battletron.engine.GameEngine;
import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerAi;
import com.alltimeslucky.battletron.engine.player.SmartPlayerAi;
import com.alltimeslucky.battletron.ui.BattletronWindow;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class bootstraps the simulation.
 */
public class Battletron {

    private GameEngine gameEngine;
    private BattletronWindow window;

    /**
     * Main entry point into the application.
     * @param args Program arguments
     */
    public static void main(String[] args) {
        new Battletron().play();
    }

    /**
     * Constructor. Creates the core components for the simulation.
     */
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

    /**
     * Starts the simulation.
     */
    public void play() {
        gameEngine.start();
        System.out.println("Complete");
    }

    /**
     * Creates an instance of GameEngine will all its dependencies.
     * @return
     */
    private GameEngine createEngine() {
        Player player1 = new Player(1, 0, 0);
        Player player2 = new Player(2, 99, 99);
        PlayerAi player1Controller = new SmartPlayerAi(player1);
        PlayerAi player2Controller = new SmartPlayerAi(player2);
        List<GameStateListener> gameStateListeners = Arrays.asList(window.getGameStateListener(), new PrintListener());
        GameEngine gameEngine = new GameEngine(gameStateListeners, player1, player2, player1Controller, player2Controller);
        return gameEngine;
    }
}
