package com.alltimeslucky.battletron.player.controller;

import com.alltimeslucky.battletron.game.model.Game;

public class RemoteAiPlayerController implements PlayerController {

    private String url;

    RemoteAiPlayerController(String url) {

        this.url = url;
    }

    @Override
    public void execute(Game game) {

    }
}
