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
     * @param playerType The type of player
     * @param playerId The id of the player websocket.
     * @param player The instance of player
     * @return an instance of PlayerController
     */
    public PlayerController getPlayerController(String playerType, String playerId, Player player) {
        switch (playerType) {
            case "w-a-s-d": return new WebSocketLeftKeysPlayerController(player, clientWebSocketRepository.get(playerId));
            case "arrowKeys": return new WebSocketRightKeysPlayerController(player, clientWebSocketRepository.get(playerId));
            case "keyboard": return new WebSocketPlayerController(player, clientWebSocketRepository.get(playerId));
            case "simpleAi": return new SimplePlayerAi(player);
            case "downLeftAi": return new DownLeftPlayerController(player);

            default: return null;
        }


    }
}
