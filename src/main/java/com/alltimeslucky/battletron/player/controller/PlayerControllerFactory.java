package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.player.model.Player;
import com.alltimeslucky.battletron.server.websocket.ClientWebSocket;

public class PlayerControllerFactory {

    /**
     * Returns a PlayerController implementation based on the give inputs.
     *
     * @param controllerType The type of player
     * @param clientWebSocket The user's WebSocket.
     * @param player         The instance of player
     * @return an instance of PlayerController
     */
    public PlayerController getPlayerController(PlayerControllerType controllerType, ClientWebSocket clientWebSocket, Player player) {
        switch (controllerType) {
            case KEYBOARD_WASD_KEYS:
                return new WebSocketWasdKeysPlayerController(player, clientWebSocket);
            case KEYWORD_ARROW_KEYS:
                return new WebSocketArrowKeysPlayerController(player, clientWebSocket);
            case KEYBOARD:
                return new WebSocketPlayerController(player, clientWebSocket);
            case AI_SIMPLE:
                return new SimplePlayerAi(player);
            case AI_DOWNLEFT:
                return new DownLeftPlayerController(player);
            case OPEN:
                return null;

            default:
                return null;
        }
    }
}
