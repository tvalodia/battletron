package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameListener;
import com.alltimeslucky.battletron.server.session.repository.SessionRepository;

import java.io.IOException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * This class listens for Game updates and routes them to the
 * mapped sockets.
 */
@Component
public class ClientWebSocketController implements GameListener {

    private Map<String, Long> sessionGameMap;
    private final SessionRepository sessionRepository;

    @Autowired
    public ClientWebSocketController(SessionRepository sessionRepository) {
        sessionGameMap = new ConcurrentHashMap<>();
        this.sessionRepository = sessionRepository;
    }


    @Override
    public void onGameStateUpdate(Game game) {
        sessionGameMap.forEach((sessionId, gameId) -> {
            if (game.getId() == gameId) {
                try {
                    sessionRepository.get(sessionId).getClientWebSocket().sendGameState(game);
                } catch (IOException e) {
                    e.printStackTrace();
                }
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
