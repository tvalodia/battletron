package com.alltimeslucky.battletron.game.controller;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.view.PrintGameListener;
import com.alltimeslucky.battletron.player.model.Direction;
import com.alltimeslucky.battletron.game.model.GameFactory;
import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.player.controller.PlayerController;
import com.alltimeslucky.battletron.player.controller.SimplePlayerAi;
import com.alltimeslucky.battletron.server.websocket.WebSocketGameStateRouterFactory;

import java.util.GregorianCalendar;

/**
 * A GameController factory used to create instances of GameEngines.
 */
public class GameControllerFactory {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    /**
     * Instantiates a GameController with default values.
     *
     * @return A GameController with default values
     */
    public static GameController getOnlineGameEngine() {
        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);
        Game game = GameFactory.getGameState(WIDTH, HEIGHT, player1, player2);
        game.registerListener(new PrintGameListener());
        game.registerListener(WebSocketGameStateRouterFactory.getWebSocketGameStateUpdateRouter());
        PlayerController player1Ai = new SimplePlayerAi(player1);
        PlayerController player2Ai = new SimplePlayerAi(player2);
        return new GameController(game, player1Ai, player2Ai);
    }

    /**
     * Instantiates a GameController with the specified values.
     *
     * @return An instance of GameController
     */
    public static GameController getLocalGameEngine(Player player1, Player player2,
                                                    PlayerController player1Controller, PlayerController player2Controller) {
        Game game = new Game(GregorianCalendar.getInstance().getTimeInMillis(), WIDTH, HEIGHT,
                player1, player2);
        return new GameController(game, player1Controller, player2Controller);
    }

}