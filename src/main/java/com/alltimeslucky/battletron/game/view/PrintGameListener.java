package com.alltimeslucky.battletron.game.view;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameListener;
import com.alltimeslucky.battletron.server.game.api.dto.GameDto;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class just prints a json formatted version of the Game at each tick.
 */
public class PrintGameListener implements GameListener {

    private static final Logger LOG = LogManager.getLogger();
    private ObjectMapper objectMapper;

    public PrintGameListener() {
        objectMapper = new ObjectMapper();
    }

    @Override
    public void onGameStateUpdate(Game game) {
        try {
            LOG.trace(objectMapper.writeValueAsString(new GameDto(game)));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
