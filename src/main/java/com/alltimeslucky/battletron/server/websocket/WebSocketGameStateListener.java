package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.server.api.game.GameDto;
import com.fasterxml.jackson.databind.ObjectMapper;


import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

/**
 * This class handles GameState updates and passed them on to web socket clients.
 */
public class WebSocketGameStateListener implements GameStateListener {

    private static final Logger LOG = LogManager.getLogger();

    private ConcurrentHashMap<Session, Long> sessionGameIdMap;
    private ObjectMapper objectMapper;

    public WebSocketGameStateListener() {
        sessionGameIdMap = new ConcurrentHashMap<>();
        objectMapper = new ObjectMapper();
    }

    public void registerSession(Session session, long id) {
        sessionGameIdMap.put(session, id);
    }

    public void unregisterSession(Session session) {
        sessionGameIdMap.remove(session);
    }

    @Override
    public void onGameStateUpdate(GameState gameState) {
        LOG.debug(gameState);
        Enumeration<Session> sessions = sessionGameIdMap.keys();
        while (sessions.hasMoreElements()) {
            Session session = sessions.nextElement();
            if (gameState.getId() == sessionGameIdMap.get(session)) {
                try {
                    session.getRemote().sendString(objectMapper.writeValueAsString(new GameDto(gameState)));
                    LOG.debug("GameState update sent to " + session);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    LOG.error(ioe);
                }
            }
        }
    }
}
