package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameListener;
import com.alltimeslucky.battletron.server.session.SessionRepository;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

/**
 * This class listens for Game updates and routes them to the
 * mapped sockets.
 */
public class ClientWebSocketController implements GameListener {

    private Map<String, Long> sessionGameMap;
    private final SessionRepository sessionRepository;

    @Inject
    public ClientWebSocketController(SessionRepository sessionRepository) {
        sessionGameMap = new ConcurrentHashMap<>();
        this.sessionRepository = sessionRepository;
    }


    @Override
    public void onGameStateUpdate(Game game) {
        sessionGameMap.forEach((sessionId, gameId) -> {
            if (game.getId() == gameId) {
                sessionRepository.get(sessionId).getClientWebSocket().sendGameState(game);
            }
        }
        );
    }

    public void registerForUpdates(String sessionId, long gameId) {
        sessionGameMap.put(sessionId, gameId);
    }

    public void deregisterForUpdates(String sessionId) {
        sessionGameMap.remove(sessionId);
    }
}
