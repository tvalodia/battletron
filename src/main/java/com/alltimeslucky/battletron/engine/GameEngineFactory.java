package com.alltimeslucky.battletron.engine;

import com.alltimeslucky.battletron.client.PrintGameStateListener;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.engine.player.Player;
import com.alltimeslucky.battletron.engine.player.PlayerController;
import com.alltimeslucky.battletron.engine.player.SmartPlayerController;

import java.util.Arrays;
import java.util.List;

/**
 * A GameEngine factory used to create instances of GameEngines.
 */
public class GameEngineFactory {

    /**
     * Instantiates a GameEngine with default values.
     * @return A GameEngine with default values
     */
    public static GameEngine getGameEngine() {
        Player player1 = new Player(1, 0, 0);
        Player player2 = new Player(2, 99, 99);
        PlayerController player1Ai = new SmartPlayerController(player1);
        PlayerController player2Ai = new SmartPlayerController(player2);
        List<GameStateListener> gameStateListeners = Arrays.asList(new PrintGameStateListener());
        GameEngine gameEngine = new GameEngine(gameStateListeners, player1, player2, player1Ai, player2Ai);
        return gameEngine;
    }

}