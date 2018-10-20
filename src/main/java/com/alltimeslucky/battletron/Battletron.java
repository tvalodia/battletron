package com.alltimeslucky.battletron;

import com.alltimeslucky.battletron.client.PrintGameStateListener;
import com.alltimeslucky.battletron.engine.Direction;
import com.alltimeslucky.battletron.engine.GameEngine;
import com.alltimeslucky.battletron.engine.GameEngineFactory;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.ExperimentPlayerAi;
import com.alltimeslucky.battletron.engine.player.KeyboardLeftPlayerController;
import com.alltimeslucky.battletron.engine.player.KeyboardPlayerController;
import com.alltimeslucky.battletron.engine.player.KeyboardRightPlayerController;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerController;
import com.alltimeslucky.battletron.engine.player.SimplePlayerAi;
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
        gameEngine = createTwoPlayerGame();
        window.initialise();

        try {
            Thread.sleep(1000);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    /**
     * Starts the simulation.
     */
    public void play() {
        gameEngine.start();
    }

    /**
     * Creates an instance of GameEngine will all its dependencies.
     * @return a newly create GameEngine object
     */
    private GameEngine createOnePlayerGame() {
        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);
        KeyboardPlayerController keyboardPlayerController = new KeyboardLeftPlayerController(player1);
        PlayerController player2Controller = new SimplePlayerAi(player2);
        window = new BattletronWindow(keyboardPlayerController, null);
        List<GameStateListener> gameStateListeners = Arrays.asList(window.getGameStateListener(), new PrintGameStateListener());
        return GameEngineFactory.getGameEngine(player1, player2, keyboardPlayerController, player2Controller, gameStateListeners);
    }

    private GameEngine createTwoPlayerGame() {
        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);
        KeyboardPlayerController keyboardPlayer1Controller = new KeyboardLeftPlayerController(player1);
        KeyboardPlayerController keyboardPlayer2Controller = new KeyboardRightPlayerController(player2);
        window = new BattletronWindow(keyboardPlayer1Controller, keyboardPlayer2Controller);
        List<GameStateListener> gameStateListeners = Arrays.asList(window.getGameStateListener(), new PrintGameStateListener());
        return GameEngineFactory.getGameEngine(player1, player2,
                keyboardPlayer1Controller, keyboardPlayer2Controller, gameStateListeners);
    }
}
