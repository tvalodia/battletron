package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.game.model.Game;
import com.alltimeslucky.battletron.game.model.GameListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import javax.inject.Inject;

/**
 * This class listens for Game updates and routes them to the
 * mapped sockets.
 */
public class ClientWebSocketController implements GameListener {

    private Map<String, Long> playerGameMap;
    private final ClientWebSocketRepository clientWebSocketRepository;

    @Inject
    public ClientWebSocketController(ClientWebSocketRepository clientWebSocketRepository) {
        playerGameMap = new ConcurrentHashMap<>();
        this.clientWebSocketRepository = clientWebSocketRepository;
    }


    @Override
    public void onGameStateUpdate(Game game) {
        playerGameMap.forEach((playerId, gameId) -> {
            if (game.getId() == gameId) {
                clientWebSocketRepository.get(playerId).sendGameState(game);
            }
        }
        );
    }

    public void registerForUpdates(String playerId, long gameId) {
        playerGameMap.put(playerId, gameId);
    }

    public void deregisterForUpdates(String playerId) {
        playerGameMap.remove(playerId);
    }
}
