package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocketRepository;

import javax.inject.Inject;

public class PlayerControllerFactory {

    private ClientWebSocketRepository clientWebSocketRepository;

    @Inject
    public PlayerControllerFactory(ClientWebSocketRepository clientWebSocketRepository) {
        this.clientWebSocketRepository = clientWebSocketRepository;
    }

    /**
     * Returns a PlayerController implementation based on the give inputs.
     * @param controllerType The type of player
     * @param playerId The id of the player websocket.
     * @param player The instance of player
     * @return an instance of PlayerController
     */
    public PlayerController getPlayerController(PlayerControllerType controllerType, String playerId, Player player) {
        switch (controllerType) {
            case KEYBOARD_WASD_KEYS: return new WebSocketWasdKeysPlayerController(player, clientWebSocketRepository.get(playerId));
            case KEYWORD_ARROW_KEYS: return new WebSocketArrowKeysPlayerController(player, clientWebSocketRepository.get(playerId));
            case KEYBOARD: return new WebSocketPlayerController(player, clientWebSocketRepository.get(playerId));
            case AI_SIMPLE: return new SimplePlayerAi(player);
            case AI_DOWNLEFT: return new DownLeftPlayerController(player);
            case ONLINE: return null;

            default: return null;
        }


    }
}
