package com.alltimeslucky.battletron;

import com.alltimeslucky.battletron.config.BattletronModule;
import com.alltimeslucky.battletron.game.controller.GameController;
import com.alltimeslucky.battletron.game.controller.GameControllerFactory;
import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.game.view.PrintGameListener;
import com.alltimeslucky.battletron.player.controller.KeyboardLeftPlayerController;
import com.alltimeslucky.battletron.player.controller.KeyboardPlayerController;
import com.alltimeslucky.battletron.player.controller.KeyboardRightPlayerController;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.SimplePlayerAi;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.ui.BattletronWindow;
import com.google.inject.Guice;
import com.google.inject.Injector;

/**
 * This class bootstraps the simulation.
 */
public class Battletron {

    private GameController gameController;
    private BattletronWindow window;
    private GameControllerFactory gameControllerFactory;
    private GameFactory gameFactory;

    /**
     * Main entry point into the application.
     * @param args Program arguments
     */
    public static void main(String[] args) {
        Injector injector = Guice.createInjector(new BattletronModule());
        injector.getInstance(Battletron.class).play();
    }

    /**
     * Constructor. Creates the core components for the simulation.
     */
    public Battletron(GameControllerFactory gameControllerFactory, GameFactory gameFactory) {
        this.gameControllerFactory = gameControllerFactory;
        this.gameFactory = gameFactory;

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
        Game game = gameFactory.get();
        KeyboardPlayerController keyboardPlayerController = new KeyboardLeftPlayerController(game.getPlayer1());
        PlayerController player2Controller = new SimplePlayerAi(game.getPlayer2());
        window = new BattletronWindow(keyboardPlayerController, null);

        GameController gameController = gameControllerFactory.get(game,
                keyboardPlayerController, player2Controller);
        gameController.getGame().registerListener(window.getGameStateListener());
        gameController.getGame().registerListener(new PrintGameListener());
        return gameController;
    }

    private GameController createTwoPlayerGame() {
        Game game = gameFactory.get();
        KeyboardPlayerController keyboardPlayer1Controller = new KeyboardLeftPlayerController(game.getPlayer1());
        KeyboardPlayerController keyboardPlayer2Controller = new KeyboardRightPlayerController(game.getPlayer2());
        window = new BattletronWindow(keyboardPlayer1Controller, keyboardPlayer2Controller);

        GameController gameController = gameControllerFactory.get(game,
                keyboardPlayer1Controller, keyboardPlayer2Controller);
        gameController.getGame().registerListener(window.getGameStateListener());
        gameController.getGame().registerListener(new PrintGameListener());
        return gameController;
    }

    private GameController createAiGame() {
        Game game = gameFactory.get();
        PlayerController player1Controller = new SimplePlayerAi(game.getPlayer1());
        PlayerController player2Controller = new SimplePlayerAi(game.getPlayer2());
        window = new BattletronWindow(null, null);

        GameController gameController = gameControllerFactory.get(game,
                player1Controller, player2Controller);
        gameController.getGame().registerListener(window.getGameStateListener());
        gameController.getGame().registerListener(new PrintGameListener());
        return gameController;
    }
}
