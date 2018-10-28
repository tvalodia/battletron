package com.alltimeslucky.battletron.server.websocket;

import com.alltimeslucky.battletron.engine.gamestate.GameState;
import com.alltimeslucky.battletron.engine.gamestate.GameStateListener;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * This class listens for GameState updates and routes them to the
 * mapped sockets.
 */
public class WebSocketGameStateRouter implements GameStateListener {

    private Map<String, Long> playerGameMap;
    private ClientWebSocketRepository clientWebSocketRepository;

    public WebSocketGameStateRouter() {
        playerGameMap = new ConcurrentHashMap<>();
        clientWebSocketRepository = ClientWebSocketRepository.getInstance();
    }


    @Override
    public void onGameStateUpdate(GameState gameState) {
        playerGameMap.forEach((playerId, gameId) -> {
            if (gameState.getId() == gameId) {
                clientWebSocketRepository.getClientWebSocket(playerId).sendGameState(gameState);
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
