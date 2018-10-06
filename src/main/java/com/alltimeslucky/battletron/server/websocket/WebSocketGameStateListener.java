package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;
import com.alltimeslucky.battletron.server.api.game.GameDto;
import com.google.gson.Gson;

import java.io.IOException;
import java.util.Enumeration;
import java.util.concurrent.ConcurrentHashMap;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.eclipse.jetty.websocket.api.Session;

public class WebSocketGameStateListener implements GameStateListener {

    private static final Logger LOG = LogManager.getLogger();

    private ConcurrentHashMap<Session, Long> sessionGameIds;
    private Gson gson;

    public WebSocketGameStateListener() {
        sessionGameIds = new ConcurrentHashMap<>();
        gson = new Gson();
    }

    public void registerSession(Session session, long id) {
        sessionGameIds.put(session, id);
    }

    public void unregisterSession(Session session) {
        sessionGameIds.remove(session);
    }

    @Override
    public void onGameStateUpdate(GameState gameState) {
        LOG.debug(gameState);
        Enumeration<Session> sessions = sessionGameIds.keys();
        while (sessions.hasMoreElements()) {
            Session session = sessions.nextElement();
            if (gameState.getId() == sessionGameIds.get(session)) {
                try {
                    session.getRemote().sendString(gson.toJson(new GameDto(gameState)));
                    LOG.debug("GameState update sent to " + session);
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    LOG.error(ioe);
                }
            }
        }
    }
}
