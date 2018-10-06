package com.alltimeslucky.battletron;

import com.alltimeslucky.battletron.client.PrintGameStateListener;
import com.alltimeslucky.battletron.engine.GameEngine;
import com.alltimeslucky.battletron.engine.GameEngineFactory;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.DownLeftPlayerController;
import com.alltimeslucky.battletron.engine.player.ExperimentPlayerAi;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerController;
import com.alltimeslucky.battletron.ui.BattletronWindow;

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
    }

    /**
     * Creates an instance of GameEngine will all its dependencies.
     * @return
     */
    private GameEngine createEngine() {
        Player player1 = new Player(1, 0, 0);
        Player player2 = new Player(2, 99, 99);
        PlayerController player1Controller = new ExperimentPlayerAi(player1);
        PlayerController player2Controller = new DownLeftPlayerController(player2);
        List<GameStateListener> gameStateListeners = Arrays.asList(window.getGameStateListener(), new PrintGameStateListener());
        GameEngine gameEngine = GameEngineFactory.getGameEngine(player1Controller, player2Controller, gameStateListeners);
        return gameEngine;
    }
}
