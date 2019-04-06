package com.alltimeslucky.battletron.player.controller.settings;

import com.alltimeslucky.battletron.player.controller.PlayerControllerType;

import org.springframework.stereotype.Component;

@Component
public class PlayerControllerSettingsFactory {

    public PlayerControllerSettings get(PlayerControllerType playerControllerType) {
        return new PlayerControllerSettings(playerControllerType);
    }
}
