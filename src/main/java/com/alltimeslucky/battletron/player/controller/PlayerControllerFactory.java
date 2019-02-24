package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.player.controller.remote.RemoteAiPlayerController;
import com.alltimeslucky.battletron.player.controller.settings.PlayerControllerSettings;
import com.alltimeslucky.battletron.player.model.Player;

public class PlayerControllerFactory {

    /**
     * Returns a PlayerController implementation based on the give inputs.
     *
     * @param settings  The parameters of the PlayerController
     * @param player          The instance of player
     * @return an instance of PlayerController
     */
    public PlayerController getPlayerController(PlayerControllerSettings settings, Player player) {
        switch (settings.getPlayerControllerType()) {
            case KEYBOARD_WASD_KEYS:
                return new WebSocketWasdKeysPlayerController(player, settings.getClientWebSocket());
            case KEYWORD_ARROW_KEYS:
                return new WebSocketArrowKeysPlayerController(player, settings.getClientWebSocket());
            case KEYBOARD:
                return new WebSocketPlayerController(player, settings.getClientWebSocket());
            case AI_SIMPLE:
                return new SimplePlayerAi(player);
            case AI_DOWNLEFT:
                return new DownLeftPlayerController(player);
            case AI_REMOTE:
                return new RemoteAiPlayerController(settings.getRemoteAiHost(), player);
            case OPEN:
                return null;

            default:
                return null;
        }
    }
}
