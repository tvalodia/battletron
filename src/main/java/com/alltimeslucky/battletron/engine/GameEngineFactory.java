package com.alltimeslucky.battletron.engine;

import com.alltimeslucky.battletron.client.PrintGameStateListener;
import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateFactory;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerController;
import com.alltimeslucky.battletron.engine.player.SimplePlayerAi;

import java.util.GregorianCalendar;

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
        Player player1 = new Player(1, 33, 50, Direction.RIGHT);
        Player player2 = new Player(2, 66, 50, Direction.LEFT);
        GameState gameState = GameStateFactory.getGameState(WIDTH, HEIGHT, player1, player2);
        gameState.registerListener(new PrintGameStateListener());
        PlayerController player1Ai = new SimplePlayerAi(player1);
        PlayerController player2Ai = new SimplePlayerAi(player2);
        return new GameEngine(gameState, player1Ai, player2Ai);
    }

    /**
     * Instantiates a GameEngine with the specified values.
     *
     * @return An instance of GameEngine
     */
    public static GameEngine getGameEngine(Player player1, Player player2,
                                           PlayerController player1Controller, PlayerController player2Controller) {
        GameState gameState = new GameState(GregorianCalendar.getInstance().getTimeInMillis(), WIDTH, HEIGHT,
                player1, player2);

        return new GameEngine(gameState, player1Controller, player2Controller);
    }

}