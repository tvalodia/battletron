package com.alltimeslucky.battletron.engine;

import com.alltimeslucky.battletron.client.PrintGameStateListener;
import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateFactory;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerController;
import com.alltimeslucky.battletron.engine.player.SimplePlayerAi;

import java.util.Arrays;
import java.util.GregorianCalendar;
import java.util.List;

/**
 * A GameEngine factory used to create instances of GameEngines.
 */
public class GameEngineFactory {

    private static final int WIDTH = 100;
    private static final int HEIGHT = 100;

    /**
     * Instantiates a GameEngine with default values.
     *
     * @return A GameEngine with default values
     */
    public static GameEngine getGameEngine() {
        Player player1 = new Player(1, 0, 0);
        Player player2 = new Player(2, 99, 99);
        List<GameStateListener> gameStateListeners = Arrays.asList(new PrintGameStateListener());
        GameState gameState = GameStateFactory.getGameState(WIDTH, HEIGHT, player1, player2, gameStateListeners);
        PlayerController player1Ai = new SimplePlayerAi(player1);
        PlayerController player2Ai = new SimplePlayerAi(player2);
        GameEngine gameEngine = new GameEngine(gameState, player1Ai, player2Ai);
        return gameEngine;
    }

    /**
     * Instantiates a GameEngine with default values.
     *
     * @return A GameEngine with default values
     */
    public static GameEngine getGameEngine(PlayerController player1Controller, PlayerController player2Controller,
                                           List<GameStateListener> gameStateListeners) {
        Player player1 = new Player(1, 0, 0);
        Player player2 = new Player(2, 99, 99);
        GameState gameState = new GameState(GregorianCalendar.getInstance().getTimeInMillis(), WIDTH, HEIGHT,
                player1, player2, gameStateListeners);

        GameEngine gameEngine = new GameEngine(gameState, player1Controller, player2Controller);
        return gameEngine;
    }

}