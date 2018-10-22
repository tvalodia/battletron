package com.alltimeslucky.battletron.client;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.server.api.game.GameDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class just prints a json formatted version of the GameState at each tick.
 */
public class PrintGameStateListener implements GameStateListener {

    private static final Logger LOG = LogManager.getLogger();
    private ObjectMapper objectMapper;

    public PrintGameStateListener() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onGameStateUpdate(GameState gameState) {
        try {
            LOG.trace(objectMapper.writeValueAsString(new GameDto(gameState)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
