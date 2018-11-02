package com.alltimeslucky.battletron;

import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.view.PrintGameListener;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.player.controller.KeyboardLeftPlayerController;
import com.alltimeslucky.battletron.player.controller.KeyboardPlayerController;
import com.alltimeslucky.battletron.player.controller.KeyboardRightPlayerController;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.SimplePlayerAi;
import com.alltimeslucky.battletron.ui.BattletronWindow;

/**
 * This class bootstraps the simulation.
 */
public class Battletron {

    private GameController gameController;
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
        gameController = createTwoPlayerGame();
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
        gameController.start();
    }

    /**
     * Creates an instance of GameController will all its dependencies.
     * @return a newly create GameController object
     */
    private GameController createOnePlayerGame() {
        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);
        KeyboardPlayerController keyboardPlayerController = new KeyboardLeftPlayerController(player1);
        PlayerController player2Controller = new SimplePlayerAi(player2);
        window = new BattletronWindow(keyboardPlayerController, null);
        GameController gameController = GameControllerFactory.getLocalGameEngine(player1, player2, keyboardPlayerController, player2Controller);
        gameController.getGame().registerListener(window.getGameStateListener());
        gameController.getGame().registerListener(new PrintGameListener());
        return gameController;
    }

    private GameController createTwoPlayerGame() {
        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);
        KeyboardPlayerController keyboardPlayer1Controller = new KeyboardLeftPlayerController(player1);
        KeyboardPlayerController keyboardPlayer2Controller = new KeyboardRightPlayerController(player2);
        window = new BattletronWindow(keyboardPlayer1Controller, keyboardPlayer2Controller);
        GameController gameController = GameControllerFactory.getLocalGameEngine(player1, player2,
                keyboardPlayer1Controller, keyboardPlayer2Controller);
        gameController.getGame().registerListener(window.getGameStateListener());
        gameController.getGame().registerListener(new PrintGameListener());
        return gameController;
    }

    private GameController createAiGame() {
        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);
        PlayerController player1Controller = new SimplePlayerAi(player1);
        PlayerController player2Controller = new SimplePlayerAi(player2);
        window = new BattletronWindow(null, null);
        GameController gameController = GameControllerFactory.getLocalGameEngine(player1, player2, player1Controller, player2Controller);
        gameController.getGame().registerListener(window.getGameStateListener());
        gameController.getGame().registerListener(new PrintGameListener());
        return gameController;
    }
}
